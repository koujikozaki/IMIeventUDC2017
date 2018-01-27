import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.URL;
import java.util.ArrayList;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.net.ssl.HttpsURLConnection;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;


public class EventCrawler {

	public EventCrawler(){
		
	}
	
	public static void main(String args[]) {
		System.out.println("START");
		
		EventCrawler ecr = new EventCrawler();
		ecr.test();
		
	}
	
	void test() {
		System.out.println("TEST");
		try {


//*** ���ʕϐ�	
			FileOutputStream out;
			OutputStreamWriter ow;
			BufferedWriter bw;
			
			int ID = 0;
			
//*** OIH�C�x���g�N���[��		
			out = new FileOutputStream("output/OIHevents20180126.ttl");
			ow = new OutputStreamWriter(out, "UTF-8");
			bw = new BufferedWriter(ow);
		
			//�w�b�_��������
			bw.write(this.getRDFHeader());				
			
			ID = 1;
			for(int i=1 ;i<=4;i++) {
				Document document = Jsoup.connect("https://www.innovation-osaka.jp/ja/event-calendar/?pno="+i).get();
	            String data = document.html();
	           // System.out.println(data);
	        	ID = getEventDataOIH(data, bw, ID);
			}
			
        	bw.close();

        	
//*** �Y�n�كC�x���g�N���[��		
        	out = new FileOutputStream("output/SansokanEvents20180126.ttl");
			ow = new OutputStreamWriter(out, "UTF-8");
			bw = new BufferedWriter(ow);
		
			//�w�b�_��������
			bw.write(this.getRDFHeader());				
			
			ID = 1;
			for(int i=1 ;i<=3;i++) {
				Document document = Jsoup.connect("https://www.sansokan.jp/events/?ym=2018-0"+i).get();
	            String data = document.html();
	           // System.out.println(data);
	        	ID = getEventDataSansokan(data, bw, ID, i);
			}
			
        	bw.close();	
		
		
		}
		catch(Exception e){
			System.out.println(e.toString());
		}
	}
	
//OIH�C�x���g���
	int getEventDataOIH(String data, BufferedWriter bw, int ID ) {
		System.out.println("-------");
		int start = data.indexOf("css-events-list");
		int end = data.indexOf("https://www.innovation-osaka.jp/ja/event-archive" );	
		System.out.println("start: "+start);
		System.out.println("end: "+end);
		
		int ST = start;
	//	int ID = 1;
		
		while(ST < end) {
			System.out.println("ST: "+ST+" end:"+end);
			//URL
			int eventUrlST = data.indexOf("<a href=\"",ST);
//			if(eventUrlST>end) {
//				break;
//				}
			System.out.println("eventUrlST: "+eventUrlST);
			int eventUrlEN = data.indexOf("\"",eventUrlST+9);
			//int eventUrlEN = data.indexOf("\">",eventUrlST);
			System.out.println("eventUrlEN: "+eventUrlEN);
			String eventUrl = data.substring(eventUrlST+9, eventUrlEN);
			System.out.println("eventUrl: "+eventUrl);
			if(eventUrl.startsWith("https://www.innovation-osaka.jp/ja/event-archive")) {
				break;
				}
			
			String imageKey = "class=\"attachment-200x100 size-200x100 wp-post-image\" alt=\"\" srcset=\"";
			int imageST = data.indexOf(imageKey,eventUrlEN);
			//System.out.println("eventDateST: "+eventDateST);
			int imageEN = data.indexOf("\"",imageST+imageKey.length()+1);
			//System.out.println("eventUrlEN: "+eventUrlEN);
			String image = data.substring(imageST+imageKey.length(), imageEN);
			System.out.println("image: "+image);
			
			//���t
			int eventDateST = data.indexOf("<p class=\"date\">",eventUrlEN);
			//System.out.println("eventDateST: "+eventDateST);
			int eventDateEN = data.indexOf("</p><p>",eventDateST);
			//System.out.println("eventUrlEN: "+eventUrlEN);
			String eventDate = data.substring(eventDateST+16, eventDateEN);
			System.out.println("eventDate: "+eventDate);
			
			System.out.println("--->chacktDate: "+this.checkDate(eventDate));
			
			
			//����
			int eventTitleST = eventDateEN;
			//System.out.println("eventDateST: "+eventDateST);
			int eventTitleEN = data.indexOf("</p></a>",eventTitleST);
			//System.out.println("eventUrlEN: "+eventUrlEN);
			String eventTitle = data.substring(eventTitleST+7, eventTitleEN);
			System.out.println("eventTitle: "+eventTitle);
			
			//�J�e�S��
			int eventCategoryST = data.indexOf("class=\"label\">",eventTitleEN);
			System.out.println("eventCategoryST: "+eventCategoryST);
			int eventCategoryEN = data.indexOf("</a></p>",eventCategoryST);
			System.out.println("eventCategoryEN: "+eventCategoryEN);
			String eventCategory = data.substring(eventCategoryST+14, eventCategoryEN);
			//System.out.println("eventCategoryEN: "+eventCategoryEN);


			//RDF�o��
			try {
				bw.write("<http://lodosaka.jp/IMIhack/event/OIH#"+ID+"> a ic:�C�x���g�^ ;\n");
				bw.write("    ic:���� [\n");
				bw.write("         ic:�\�L \""+eventTitle+"\" ];\n");
				bw.write("    ic:���� [\n");
				bw.write("         a ic:���ԃX�P�W���[���^ ;\n");
				bw.write("         ic:����    \""+eventDate+"\"  ;\n");
				bw.write("         ic:�J�n���� [\n");
				bw.write("            a ic:�����^ ;\n");
				String datetime = checkDateTimeXSD(eventDate);
				bw.write("            ic:�W���^���� "+datetime+" ;\n");
				bw.write("            ic:�N \""+datetime.substring(1, 5)+"\"^^xsd:integer;\n");
				bw.write("            ic:�� \""+datetime.substring(6, 8)+"\"^^xsd:integer;\n");
				bw.write("            ic:�� \""+datetime.substring(9, 11)+ "\"^^xsd:integer; ]] ;\n");
				bw.write("    ic:�J�Ïꏊ [\n");
			    bw.write("             a ic:�n���^ ;\n");
			    bw.write("             ic:�\�L \"���C�m�x�[�V�����n�u\" ; \n");
			    bw.write("             ic:�Q�� [\n");
			    bw.write("                a ic:�Q�ƌ^ ;\n");
			    bw.write("        ic:�Q�Ɛ� <https://www.innovation-osaka.jp/ja/>]];\n");
			    String[] imageList = image.split(",");
			    if(imageList.length>1) {
			    	bw.write("    ic:�摜  <"+imageList[1].replaceAll("300w", "").trim() +"> ;\n");
			    }
				bw.write("    ic:�A���� [\n");
				bw.write("         a ic:�A����^ ;\n");
				bw.write("         ic:Web�T�C�g    <"+eventUrl+"> ] .\n");

			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				System.out.println(e.toString());
			} 
			
			ID++;
			ST = eventCategoryEN;
			System.out.println("-------");
			}
		
		return ID;		
	}
	
//�Y�n�كC�x���g���
		int getEventDataSansokan(String data, BufferedWriter bw, int ID, int month) {
			System.out.println("----Sansokan---");
			int ST = 0;
			String Day = "1";
			while(true) {
				System.out.println("--------");
				int DayST = data.indexOf("<td class=\"right\"><span style=\"font-size:13px;\"><b>",ST);
				System.out.println("DayST: "+DayST);
				if(DayST < 0) {
					break;
				}
				int DayEN = data.indexOf("</b></span></td>",DayST );	
				System.out.println("DayEN: "+DayEN);
				String Daytemp = data.substring(DayST+"<td class=\"right\"><span style=\"font-size:13px;\"><b>".length(), DayEN);
				if(!Daytemp.equals("&nbsp;")) {
					Day = Daytemp;
				}
				System.out.println("��Day: "+Day);
				
				int WkST = data.indexOf("<td class=\"center\"><span style=\"font-size:13px;\">",DayEN );	
				
				int HourST = data.indexOf("<td class=\"center\">", WkST+10 );//10�͓K��	
				int HourEN = data.indexOf("</td>",HourST );	
				//System.out.println("DayEN: "+DayEN);
				String Hour = data.substring(HourST+"<td class=\"center\">".length(), HourEN);
				System.out.println("Hour: "+Hour);
				
				
				
				int UrlST = data.indexOf("/events/eve_detail.san?H_A_NO=",DayEN);
				System.out.println("UrlST: "+UrlST);
				int UrlEN = data.indexOf("\">",UrlST);
				System.out.println("UrlEN:"+UrlEN);
				
				boolean existEvent = false;
				
				if(UrlEN == UrlST + "/events/eve_detail.san?H_A_NO=".length()) {
					System.out.println("�C�x���g�Ȃ�");
				}
				else {
					System.out.println("�C�x���g����I�I");
					existEvent = true;
					}
//					int DayNEXT = data.indexOf("<td class=\"right\"><span style=\"font-size:13px;\"><b>",DayEN);
//					System.out.println("DayNEXT:"+DayNEXT);
					//�C�x���g�P�̎��̏���
					String eventID = data.substring(UrlST+"/events/eve_detail.san?H_A_NO=".length(),UrlEN);
					System.out.println("eventID:"+eventID);
					
					int TitleEN = data.indexOf("</a></td>",UrlEN);
					String Title = data.substring(UrlEN+2,TitleEN).replaceAll("&nbsp;", "").replaceAll("<br>", "");
					System.out.println("Title:"+Title);
					
					int JoinST = data.indexOf("<td class=\"center\">",TitleEN);
					int JoinEN = data.indexOf("</td>",JoinST);
					String Join = data.substring(JoinST+"<td class=\"center\">".length(),JoinEN)
							.replaceAll("<span style=\"color:blue; font-size:13px;\"><b>", "").replaceAll("</b></span>", "");
					System.out.println("Join:"+Join);
					
					int FeeST = data.indexOf("<td class=\"center\"><span style=\"font-size:13px;\">",JoinEN);
					int FeeEN = data.indexOf("</span></td>",FeeST);
					String Fee = data.substring(FeeST+"<td class=\"center\"><span style=\"font-size:13px;\">".length(),FeeEN);
					System.out.println("Fee:"+Fee);
					
					int PlaceST = data.indexOf("<td class=\"center\">",FeeEN);
					int PlaceEN = data.indexOf("</td>",PlaceST);
					String Place = data.substring(PlaceST+"<td class=\"center\">".length(),PlaceEN);
					System.out.println("Place:"+Place);

					ST = PlaceEN;
						
				ID++;
					
			

			//RDF�o��
			if(existEvent) {
				try {
					bw.write("<http://lodosaka.jp/IMIhack/event/Sansokan#"+ID+"> a ic:�C�x���g�^ ;\n");
					//bw.write("<http://lodosaka.jp/IMIhack/event/Sansokan#"+eventID+"> a ic:�C�x���g�^ ;\n");
					bw.write("    ic:���� [\n");
					bw.write("         ic:�\�L \""+Title+"\" ];\n");
//					bw.write("    ic:���� [\n");
//					bw.write("         a ic:���ԃX�P�W���[���^ ;\n");
//					bw.write("         ic:����    \"2017"+month+"-"+Day+" "+Hour+"\" ] ;\n");

					bw.write("    ic:���� [\n");
					bw.write("         a ic:���ԃX�P�W���[���^ ;\n");
					String eventDate = "2018/0"+month+"/"+Day+" "+Hour;
					bw.write("         ic:����    \""+eventDate+"\"  ;\n");
					bw.write("         ic:�J�n���� [\n");
					bw.write("            a ic:�����^ ;\n");
					String datetime = checkDateTimeXSD(eventDate);
					bw.write("            ic:�W���^���� "+datetime+" ;\n");
					bw.write("            ic:�N \""+datetime.substring(1, 5)+"\"^^xsd:integer;\n");
					bw.write("            ic:�� \""+datetime.substring(6, 8)+"\"^^xsd:integer;\n");
					bw.write("            ic:�� \""+datetime.substring(9, 11)+ "\"^^xsd:integer; ]] ;\n");
					bw.write("    ic:�J�Ïꏊ [\n");
				    bw.write("             a ic:�n���^ ;\n");
				    bw.write("             ic:�\�L \""+Place+"\" ; \n");
				    bw.write("             ic:�Q�� [\n");
				    bw.write("                a ic:�Q�ƌ^ ;\n");
				    bw.write("        ic:�Q�Ɛ� <"+getPlaceURL(Place)+">]];\n");

					
					bw.write("    ic:�A���� [\n");
					bw.write("         a ic:�A����^ ;\n");
					bw.write("         ic:Web�T�C�g    <https://www.sansokan.jp/events/eve_detail.san?H_A_NO="+eventID+"> ] .\n");
					
					
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					System.out.println(e.toString());
				} 
				
				ID++;
//				ST = eventCategoryEN;
				System.out.println("-------");
				}
			}
			
			
			return ID;	
		}	
	
	String getRDFHeader(){
		String header =
				 "@prefix rdf:  <http://www.w3.org/1999/02/22-rdf-syntax-ns#> .\n"
				+"@prefix rdfs: <http://www.w3.org/2000/01/rdf-schema#> .\n"
				+"@prefix cc:   <http://creativecommons.org/ns#> .\n"
				+"@prefix xsd:  <http://www.w3.org/2001/XMLSchema#> .\n"
			//	+"@prefix rss:  <http://purl.org/rss/2.0/> .\n"
				+"@prefix ic:   <http://imi.go.jp/ns/core/rdf#> .\n"
				+"<>\n"
				+"	cc:attributionName \"�Í�W�i\"@ja ;\n"
				+"	cc:license <http://creativecommons.org/publicdomain/zero/1.0/deed.ja> .\n";
				
		return header;
	}
	

	 String checkDate(String str) {
		 String regex = "2018/[0-9][0-9]/[0-9][0-9]";
		 Pattern p = Pattern.compile(regex);
		 Matcher m = p.matcher(str);
		 if(m.find()) {
			 return m.group();
		 }
		 
		 return null;
	 }


	 String checkTime(String str) {
		 String regexDate = "[0-2][0-9]:[0-6][0-9]";
		 Pattern p = Pattern.compile(regexDate);
		 Matcher m = p.matcher(str);
		 if(m.find()) {
			 return m.group();
			 }
		 return null;
	 }

	 String checkDateTimeXSD(String str) {
		 String date = checkDate(str);
		 String time = checkTime(str);
		 if(date!=null) {
			 date = date.replaceAll("/", "-");
		 }
		 else {
			 date = "2017-10-01"; //���u��
		 }
		 
		 if(time!=null) {
			 time = time+":00";
		 }
		 else {
			 time = "00:00:00";			 
		 }
		 
		// "2015-01-09T18:43:09Z"^^xsd:dateTime
		 
		 return "\""+date+"T"+time+"Z\"^^xsd:dateTime";
	 }
	 
	 String getPlaceURL(String str) {
		 if(str.equals("���C�m�x�[�V�����n�u")) {
			 return "https://www.innovation-osaka.jp/ja/";			 
		 }
		 else if(str.equals("�Y�n��")) {
			 return "https://www.sansokan.jp/";
		 }  
		 else if(str.equals("Mebic")) {
			 return "http://www.mebic.com/";			 
		 }  
		 else if(str.equals("iMedio")) {
			 return "http://www.imedio.or.jp/";			 
		 }  
		
		 return "https://www.innovation-osaka.jp/ja/";
	 }
	 
}
