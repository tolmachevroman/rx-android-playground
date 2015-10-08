package sample.rxandroid.ui;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;
import sample.rxandroid.R;
import sample.rxandroid.network.Job;

/**
 * Created by romantolmachev on 7/10/15.
 */
public class JobAdapter extends BaseAdapter {

    private ArrayList<Job> jobs;

    private final LayoutInflater inflater;

    private Activity context;

    public JobAdapter(@NonNull Activity context) {
        this.jobs = new ArrayList<>();
        this.inflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return jobs.size();
    }

    @Override
    public Job getItem(int i) {
        return jobs.get(i);
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    public void addJob(Job job) {
        jobs.add(job);
    }

    public void clearJobs() {
        jobs.clear();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;

        if (convertView == null) {
            convertView = inflater.inflate(R.layout.jobs_adapter_item, parent, false);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        String jobTitle = getItem(position).getPositionTitle();
        holder.jobTitle.setText( jobTitle.substring(0, 1).toUpperCase() + jobTitle.substring(1, jobTitle.length()).toLowerCase());

        return convertView;
    }

    static class ViewHolder {

        @Bind(R.id.job_title)
        TextView jobTitle;

        public ViewHolder(View v) {
            ButterKnife.bind(this, v);
        }

    }
}