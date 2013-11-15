package edu.oit.isr;

import java.io.File;
import java.io.IOException;
import java.util.Date;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import com.csvreader.CsvWriter;

import edu.oit.isr.model.Book;

public class AmazonTopBooks_CN {
	// constants
		public static String URL_AMAZON_ROOT = "http://www.amazon.cn/gp/bestsellers/books";
		public static String DIR_DATA = "data/";
		public static String URL_BOOK= "http://www.amazon.cn/dp/";
		/**
		 * @param args
		 * @throws Exception 
		 */
		public static void main(String[] args) throws Exception {
			
			CsvWriter cw = new CsvWriter(DIR_DATA+"data_cn.csv");
			// check directory
			File dirData = new File(DIR_DATA);
			if(!dirData.isDirectory()){
				if(!dirData.mkdir()){
					throw new Exception("No directory found for saving data!"); 
				}
			}
			
				
				for(int page_num=1;page_num<2;page_num++){
					String listURL = URL_AMAZON_ROOT +"?pg="+page_num;
					System.out.println("parsing.. " + listURL);
				
					try {
						
						Document docRoot = Jsoup.connect(listURL).userAgent("Mozilla").timeout(5000).get();
						//System.out.println(docRoot);
						for(Element ele : docRoot.select(".zg_item_normal")){
							//System.out.println(ele);
							Book book = new Book();
							book.setDate(new Date());
							Element rank = ele.getElementsByClass("zg_rankNumber").first();
							book.setRank(Integer.parseInt(rank.text().replace(".", "")));
							
							
							Element title = ele.getElementsByClass("zg_title").first().getElementsByTag("a").first();
							System.out.println(title.text());
							book.setTitle(title.text());
							book.setBookUrl(title.attr("href"));
							int s = book.getBookUrl().indexOf("dp");
							book.setIsbn(book.getBookUrl().substring(s+3,s+13));
							try{
								System.out.println(book.getIsbn());
								
								Element author = ele.getElementsByClass("zg_byline").first();
								book.setAuthor(author.text().replace("by ", ""));
							
							
								Element reviews = ele.getElementsByClass("zg_reviews").first().getElementsByTag("a").last(); //a
								book.setReviews(Integer.parseInt(reviews.text().replace(",", "")));
								
								Element bindingPlatform = ele.getElementsByClass("zg_bindingPlatform").first();
								book.setBindingPlatform(bindingPlatform.text());
								
								Element releaseDate = ele.getElementsByClass("zg_releaseDate").first();
								book.setReleaseDate(releaseDate.text());
								
								
								Element amazonPrice = ele.getElementsByClass("price").first().getElementsByTag("span").first(); //a
								System.out.println();
								book.setAmazonPrice(Float.parseFloat(amazonPrice.text().replace("гд ", "")));
								System.out.println("price is "+book.getAmazonPrice());
							
//								Element lowestUsedPrice = ele.getElementsByClass("zg_usedPrice").first().getElementsByTag("span").first();//a.toInt()
//								book.setLowestUsedPrice(Float.parseFloat(lowestUsedPrice.text().replace("гд  ", "")));
								Element  sellers= ele.getElementsByClass("priceBlock").first().getElementsByTag("a").first();
								int i = sellers.text().indexOf(" ");
								book.setSellers(Integer.parseInt(sellers.text().replaceAll("[^0-9]", ",").split(",")[0]));
							}catch(Exception e){
								e.printStackTrace();
							}
							System.out.println(book);
							try{
								Document bookRoot = Jsoup.connect(URL_BOOK+book.getIsbn()).userAgent("Mozilla").timeout(5000).get();
								System.out.println(bookRoot.select("#btAsinTitle").text());
								String bookTitle = bookRoot.select("#btAsinTitle").first().text();
								book.setTitle(bookTitle);
								String review = bookRoot.select(".crAvgStars").first().getElementsByTag("a").last().text();
								try{
								book.setReviews(Integer.parseInt(review.split(" ")[0].replace(",", "")));
								}catch(NumberFormatException e){
									e.printStackTrace();
								}
								cw.write("ca");
								cw.write(book.getDate().toString());
								cw.write(Integer.toString(book.getRank()));
								cw.write(book.getAuthor());
								cw.write(book.getBindingPlatform());
								cw.write(book.getIsbn());
								cw.write(book.getTitle());
								cw.write(Float.toString(book.getAmazonPrice()));
								cw.write(Float.toString(book.getLowestUsedPrice()));
								cw.write(Integer.toString(book.getReviews()));
								try{
								for(Element c : bookRoot.select("#SalesRank")){
									String catagories = c.getElementsByClass("zg_hrsr_item").first().text().replace("in?", "");
									System.out.println(c.text());
									String[] strs = catagories.split(">");
									
									if(strs.length==6){
										cw.write(strs[1]);
										cw.write(strs[2]);
										cw.write(strs[3]);
										cw.write(strs[4]);
										cw.write(strs[5]);
									}else if(strs.length==5){
										cw.write(strs[1]);
										cw.write(strs[2]);
										cw.write(strs[3]);
										cw.write(strs[4]);
									} else if(strs.length==4){
										cw.write(strs[1]);
										cw.write(strs[2]);
										cw.write(strs[3]);
									}else if(strs.length==3){
									cw.write(strs[1]);
									cw.write(strs[2]);
									}else if(strs.length==2){
										cw.write(strs[1]);
									}
								}
								}catch(Exception e){
									e.printStackTrace();
								}
//								System.out.println(bookRoot.select(".overallRank").html());
//								System.out.println(bookRoot.select(".nodeRank").text());
							} catch (IOException e) { System.err.println("Error: " + e.getMessage()); }
							
							cw.flush();
							cw.endRecord();
						
						}
							
					} catch (IOException e) { System.err.println("Error: " + e.getMessage()); }
					
				}
			
		}
		

}
