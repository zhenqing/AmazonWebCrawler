package edu.oit.isr;

import java.io.File;
import java.io.IOException;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import edu.oit.isr.model.Book;

public class AmazonTopBooks_IN {
	// constants
		public static String URL_AMAZON_ROOT = "http://www.amazon.in/gp/bestsellers/books#";
		public static String DIR_DATA = "data/";
		public static String URL_BOOK= "http://www.amazon.in/dp/";
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
				
				for(int page_num=1;page_num<2;page_num++){
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
							
							
							Element title = ele.getElementsByClass("zg_title").first().getElementsByTag("a").first();
							book.setBookUrl(title.attr("href"));
							int s = book.getBookUrl().indexOf("dp");
							book.setIsbn(book.getBookUrl().substring(s+3,s+13));
						
							System.out.println(book.getIsbn());
							try{
								Document bookRoot = Jsoup.connect(URL_BOOK+book.getIsbn()).userAgent("Mozilla").timeout(5000).get();
//								System.out.println(bookRoot.select("#btAsinTitle").text());
//								String bookTitle = bookRoot.select("#title").first().getElementsByTag("h1").text();
//								book.setTitle(bookTitle);
//								
								for(Element c : bookRoot.select(".content ul")){
									System.out.println(c.text());
								}
//								System.out.println(bookRoot.select(".overallRank").html());
//								System.out.println(bookRoot.select(".nodeRank").text());
							} catch (IOException e) { System.err.println("Error: " + e.getMessage()); }
							
					
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
