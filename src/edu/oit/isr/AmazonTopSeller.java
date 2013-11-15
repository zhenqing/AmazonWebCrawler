package edu.oit.isr;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.regex.Pattern;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import edu.oit.isr.model.Book;

public class AmazonTopSeller {
	// constants
		public static String URL_AMAZON_ROOT = "http://www.amazon.com/gp/bestsellers/books#";
		public static String DIR_DATA = "data";
		public static String URL_BOOK= "http://www.amazon.com/dp/";
		/**
		 * @param args
		 * @throws Exception 
		 */
		public static void main(String[] args) throws Exception {
			
			
			// check directory
			File dirData = new File(DIR_DATA);
			if(!dirData.isDirectory()){
				if(!dirData.mkdir()){
					throw new Exception("No directory found for saving data!"); 
				}
			}
			
			// print category URL
				
				for(int page_num=1;page_num<6;page_num++){
					String listURL = URL_AMAZON_ROOT +page_num;
					System.out.println("parsing.. " + listURL);
				
					try {
						
						Document docRoot = Jsoup.connect(listURL).userAgent("Mozilla").timeout(5000).get();
						//System.out.println(docRoot);
						for(Element ele : docRoot.select(".zg_itemImmersion")){
							//System.out.println(ele);
							Book book = new Book();
						
							Element rank = ele.getElementsByClass("zg_rankNumber").first();
							book.setRank(Integer.parseInt(rank.text().replace(".", "")));
							
							Element imgUrl = ele.getElementsByClass("zg_itemImageImmersion").first().getElementsByTag("a").first();//src
							book.setImgUrl(imgUrl.attr("href"));
						
							Element title = ele.getElementsByClass("zg_title").first().getElementsByTag("a").first();
							book.setTitle(title.text());
							
							book.setBookUrl(title.attr("href"));
						
							int s = book.getBookUrl().indexOf("dp");
							
							book.setIsbn(book.getBookUrl().substring(s+3,s+13));
							
							Element author = ele.getElementsByClass("zg_byline").first();
							book.setAuthor(author.text().replace("by ", ""));
						
							Element reviews = ele.getElementsByClass("zg_reviews").first().getElementsByTag("a").last(); //a
							book.setReviews(Integer.parseInt(reviews.text().replace(",", "")));
							
							Element bindingPlatform = ele.getElementsByClass("zg_bindingPlatform").first();
							book.setBindingPlatform(bindingPlatform.text());
							
							Element amazonPrice = ele.getElementsByClass("zg_price").first().getElementsByTag("strong").first(); //a
							book.setAmazonPrice(Float.parseFloat(amazonPrice.text().replace("$", "")));
							
							
							Element lowestUsedPrice = ele.getElementsByClass("zg_usedPrice").first().getElementsByTag("span").first();//a.toInt()
							book.setLowestUsedPrice(Float.parseFloat(lowestUsedPrice.text().replace("$", "")));
							
							Element  sellers= ele.getElementsByClass("zg_usedPrice").first().getElementsByTag("a").first();
							int i = sellers.text().indexOf(" ");
							book.setSellers(Integer.parseInt(sellers.text().substring(0,i)));
							System.out.println(book.toString());
							//System.out.println(Integer.parseInt(sellers.text().substring(0,i)));
							
						/*
							Book book = new Book();
							

							File strFile = new File(DIR_DATA + book.getIsbn());
							if(strFile.exists()){
								System.err.println("- the book isbn " + book.getIsbn() + " is already crawled. skip.");
								continue;
							}
							// - create a file to the given directory
							FileWriter fw = new FileWriter(strFile);
							fw.write(
								book.getAuthor() + "\n" +
								book.getBindingPlatform()+ "\n" +
								book.getCatagory() + "\n" +
								book.getTitle()
							);
							fw.close();
							System.out.println("book file created");
							*/
						}
					} catch (IOException e) { System.err.println("Error: " + e.getMessage()); }
					
				}
			
		}
		

}
