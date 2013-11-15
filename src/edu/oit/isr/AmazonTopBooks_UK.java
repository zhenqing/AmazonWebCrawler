package edu.oit.isr;

import java.io.File;
import java.io.IOException;
import java.util.Date;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import com.csvreader.CsvWriter;

import edu.oit.isr.model.Book;

public class AmazonTopBooks_UK {
	// constants
		public static String URL_AMAZON_ROOT = "http://www.amazon.co.uk/gp/bestsellers/books";
		public static String DIR_DATA = "data/";
		public static String URL_BOOK= "http://www.amazon.co.uk/dp/";
		/**
		 * @param args
		 * @throws Exception 
		 */
		public static void main(String[] args) throws Exception {
			
			CsvWriter cw = new CsvWriter(DIR_DATA+"data_uk.csv");
			
			// check directory
			File dirData = new File(DIR_DATA);
			if(!dirData.isDirectory()){
				if(!dirData.mkdir()){
					throw new Exception("No directory found for saving data!"); 
				}
			}
			
			// print category URL
				
				for(int page_num=1;page_num<6;page_num++){
					String listURL = URL_AMAZON_ROOT +"?pg="+page_num;
					System.out.println("parsing.. " + listURL);
				
					try {
						
						Document docRoot = Jsoup.connect(listURL).userAgent("Mozilla").timeout(5000).get();
						//System.out.println(docRoot);
						for(Element ele : docRoot.select(".zg_itemImmersion")){
							//System.out.println(ele);
							Book book = new Book();
						
							Element rank = ele.getElementsByClass("zg_rankNumber").first();
							book.setRank(Integer.parseInt(rank.text().replace(".", "")));
							
							
							Element title = ele.getElementsByClass("zg_title").first().getElementsByTag("a").first();
							book.setTitle(title.text());
							book.setBookUrl(title.attr("href"));
							int s = book.getBookUrl().indexOf("dp");
							book.setIsbn(book.getBookUrl().substring(s+3,s+13));
						
							try{
								book.setDate(new Date());
								
								System.out.println(book.getIsbn());
								Element author = ele.getElementsByClass("zg_byline").first();
								book.setAuthor(author.text().replace("by ", ""));
							
								Element reviews = ele.getElementsByClass("zg_reviews").first().getElementsByTag("a").last(); //a
								book.setReviews(Integer.parseInt(reviews.text().replace(",", "")));
								
								Element bindingPlatform = ele.getElementsByClass("zg_bindingPlatform").first();
								book.setBindingPlatform(bindingPlatform.text());
								
								Element amazonPrice = ele.getElementsByClass("zg_price").first().getElementsByTag("strong").first(); //a
								book.setAmazonPrice(Float.parseFloat(amazonPrice.text().replace("£", "")));
								
								
								Element lowestUsedPrice = ele.getElementsByClass("zg_usedPrice").first().getElementsByTag("span").first();//a.toInt()
								book.setLowestUsedPrice(Float.parseFloat(lowestUsedPrice.text().replace("£", "")));
								Element  sellers= ele.getElementsByClass("zg_usedPrice").first().getElementsByTag("a").first();
								int i = sellers.text().indexOf(" ");
								book.setSellers(Integer.parseInt(sellers.text().replaceAll("[^0-9]", ",").split(",")[0]));
							
							}catch(Exception e){
								e.printStackTrace();
							}
							try{
								Document bookRoot = Jsoup.connect(URL_BOOK+book.getIsbn()).userAgent("Mozilla").timeout(5000).get();
//								
								String bookTitle = bookRoot.select("#btAsinTitle").first().text();
								book.setTitle(bookTitle);
								String review = bookRoot.select(".crAvgStars").first().getElementsByTag("a").last().text();
								try{
								book.setReviews(Integer.parseInt(review.split(" ")[0].replace(",", "")));
								}catch(NumberFormatException e){
									e.printStackTrace();
								}
								try{
								cw.write("uk");
								cw.write(book.getDate().toString());
								cw.write(Integer.toString(book.getRank()));
								cw.write(book.getAuthor());
								cw.write(book.getBindingPlatform());
								cw.write(book.getIsbn());
								cw.write(book.getTitle());
								cw.write(Float.toString(book.getAmazonPrice()));
								cw.write(Float.toString(book.getLowestUsedPrice()));
								cw.write(Integer.toString(book.getReviews()));
								}catch(Exception e){
									e.printStackTrace();
								}
								for(Element c : bookRoot.select("#SalesRank")){
									//System.out.println(c);
									String catagories = c.getElementsByClass("zg_hrsr_ladder").first().text().replace("in?", "");
									System.out.println(c.text());
									String[] strs = catagories.split(">");
									if(strs.length==4){
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
								for(Element c : bookRoot.select(".content ul")){
									System.out.println(c.text());
								}
//								System.out.println(bookRoot.select(".overallRank").html());
//								System.out.println(bookRoot.select(".nodeRank").text());
							} catch (IOException e) { System.err.println("Error: " + e.getMessage()); }
							finally{
								cw.flush();
								cw.endRecord();
							}
							
					
						
						}
					} catch (IOException e) { System.err.println("Error: " + e.getMessage()); }
					
				}
			cw.close();
		}
		

}
