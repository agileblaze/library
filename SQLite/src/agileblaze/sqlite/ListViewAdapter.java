package agileblaze.sqlite;

import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

public class ListViewAdapter extends ArrayAdapter<RowItem> {

	Context context;

	public ListViewAdapter(Context context, int resourceId, List<RowItem> items) {
		super(context, resourceId, items);
		this.context = context;
	}

	/* private view holder class */
	private class ViewHolder {
		ImageView ivbook;
		TextView tvcategory, tvbookname, tvprice, tvauthor, tvkey, tvdate;
		RatingBar rtBook;
	}

	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder = null;
		RowItem rowItem = getItem(position);
		String rtbook_valuestr = "";
		float rtbookfloat;
		LayoutInflater mInflater = (LayoutInflater) context
				.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
		if (convertView == null) {
			convertView = mInflater.inflate(R.layout.listviewelements, null);
			holder = new ViewHolder();
			holder.tvkey = (TextView) convertView.findViewById(R.id.tvkey);
			holder.tvcategory = (TextView) convertView
					.findViewById(R.id.Category);
			holder.tvbookname = (TextView) convertView
					.findViewById(R.id.book_name);
			holder.tvprice = (TextView) convertView.findViewById(R.id.price);
			holder.tvauthor = (TextView) convertView.findViewById(R.id.author);
			holder.ivbook = (ImageView) convertView.findViewById(R.id.ivbook);
			holder.tvdate = (TextView) convertView.findViewById(R.id.date);
			holder.rtBook = (RatingBar) convertView.findViewById(R.id.rtBook);
			convertView.setTag(holder);
		} else
			holder = (ViewHolder) convertView.getTag();
		holder.tvkey.setText(rowItem.getkey());
		holder.tvcategory.setText(rowItem.getCategory());
		holder.tvbookname.setText(rowItem.getbookname());
		holder.tvprice.setText(rowItem.getprice());
		holder.tvauthor.setText(rowItem.getauthor());
		holder.ivbook.setImageBitmap(rowItem.getImageId());
		holder.tvdate.setText(rowItem.getdate());
		rtbook_valuestr = rowItem.getRating().toString();
		rtbookfloat = Float.parseFloat(rtbook_valuestr);
		holder.rtBook.setRating(rtbookfloat);
		return convertView;
	}
}