package agileblaze.sqlite;

import android.graphics.Bitmap;

public class RowItem {
	private String key;
	private String category;
	private String bookname;
	private String price;
	private String author;
	private Bitmap bukimage;
	private String date;
	private String rating;

	public RowItem(String key, String category, String bookname, String price,
			String author, Bitmap bukimage, String date, String rating) {
		this.key = key;
		this.category = category;
		this.bookname = bookname;
		this.price = price;
		this.author = author;
		this.bukimage = bukimage;
		this.date = date;
		this.rating = rating;
	}

	public String getRating() {
		return rating;

	}

	public void setRating(String rating) {
		this.rating = rating;
	}

	public String getkey() {
		return key;

	}

	public void setkey(String key) {
		this.key = key;
	}

	public String getdate() {
		return date;

	}

	public void setdate(String date) {
		this.date = date;
	}

	public Bitmap getImageId() {
		return bukimage;
	}

	public void setImageId(Bitmap bukimage) {
		this.bukimage = bukimage;
	}

	public String getCategory() {
		return category;
	}

	public void setCategory(String category) {
		this.category = category;
	}

	public String getbookname() {
		return bookname;
	}

	public void setTitle(String bookname) {
		this.bookname = bookname;
	}

	public String getprice() {
		return price;
	}

	public void setprice(String price) {
		this.price = price;
	}

	public String getauthor() {
		return author;
	}

	public void setauthor(String author) {
		this.author = author;
	}

	@Override
	public String toString() {
		return category + "\n" + bookname + "\n" + price + "\n" + author;
	}
}
