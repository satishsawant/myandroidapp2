package realizer.com.mysurvey.surveylist;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;



import java.util.ArrayList;

import realizer.com.mysurvey.R;
import realizer.com.mysurvey.utils.FontManager;

/**
 * Created by shree on 12/7/2016.
 */
public class SurveyListAdapter extends BaseAdapter {

    private static ArrayList<SurveyListModel> pList;
    private LayoutInflater publicholidayDetails;
    private Context context1;
    View convrtview;



    public SurveyListAdapter(Context context, ArrayList<SurveyListModel> dicatationlist) {
        pList = dicatationlist;
        publicholidayDetails = LayoutInflater.from(context);
        context1 = context;
    }

    @Override
    public int getCount() {
        return pList.size();
    }

    @Override
    public Object getItem(int position) {

        return pList.get(position);
    }

    @Override
    public long getItemId(int position) {

        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;

        convrtview = convertView;
        if (convertView == null) {
            convertView = publicholidayDetails.inflate(R.layout.survey_list_adapter_layout, null);
            holder = new ViewHolder();
            holder.name = (TextView) convertView.findViewById(R.id.surveynmae);
            holder.nextArrow= (TextView) convertView.findViewById(R.id.next_arrow);
            holder.descripton = (TextView) convertView.findViewById(R.id.surveydescription);
            holder.surveyListbg= (LinearLayout) convertView.findViewById(R.id.survey_list_bg);

            holder.nextArrow.setTypeface(FontManager.getTypeface(context1, FontManager.FONTAWESOME));
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        holder.name.setText( pList.get(position).getSurveyName());
        holder.descripton.setText( pList.get(position).getSurveyDescription());
        if (pList.get(position).getListGetFrom().equals("Survey"))
        {
            if (pList.get(position).getSurveyStatus().equals("Complete"))
            {
                holder.surveyListbg.setBackgroundColor(Color.rgb(116,165,167));
            }
        }
        return convertView;
    }

    static class ViewHolder {

        TextView name,descripton,nextArrow;
        LinearLayout surveyListbg;

    }
}
