# 共通語彙基盤を用いたイベントデータ From イベントカレンダー
実践！共通語彙基盤ワークショップ・ハッカソン（2017/10/8-10/9開催）で作成した，共通語彙基盤（IMI）対応のイベントデータの公開用レポジトリ．  
UDC2017応募用に整理したバージョンです．

## データ概要
Webサイト上の複数の「イベントカレンダー」から，イベント情報を自動収集し，IMI対応の形式に変換したものです．  
データは，
1. API（SPARQLエンドポイント）
1. データファイル（Turtle形式）
1. URLでの参照解決可能なアクセス

の3通りの形態で提供し，利用法のサンプルとして，
1. Line@を使った検索用Botサービス
1. Webサイトでの簡易検索サービス

を公開しています．

## 元データ
元データとした「イベントカレンダー」は，以下の通りです．
1. 大阪イノベーションハブ（https://www.innovation-osaka.jp/ja/event-calendar/ ）
1. 大阪産業創造館（https://www.sansokan.jp/events/ ）

## データの公開場所
元データとした「イベントカレンダー」は，以下の通りです．
1. API（SPARQLエンドポイント）　
	1. http://lod.hozo.jp/repositories/IMIhack AllegroGraph）
	1. http://27.134.254.189:8890/sparql (Virtuoso）
1. データファイル（Turtle形式） https://github.com/koujikozaki/IMIeventUDC2017/tree/master/data
1. URLでの参照解決可能なアクセス
	1. http://lodosaka.jp/IMIhack/event/OIH
	1. http://lodosaka.jp/IMIhack/event/Sansokan   
	※HTML, Turtle, RDF/XML, JSON, JSON-LDに対応   
	[Simple LODI : Simple Linked Open Data Interface](https://github.com/uedayou/simplelodi)を利用しています．  

## サンプルアプリケーション
1. LINEによる検索ボット（デモ版）  
https://github.com/koujikozaki/IMIeventUDC2017/blob/master/doc/DemoQR.png  
1. 簡易検索用Webサービス  
http://lodosaka.jp/IMIhack/eventList.html  

## 参考資料  
「実践！共通語彙基盤ワークショップ・ハッカソン」での[プレゼン資料](https://github.com/koujikozaki/IMIeventUDC2017/blob/master/doc/IMIhackDoc.pdf)


 
