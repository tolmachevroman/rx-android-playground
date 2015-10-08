package sample.rxandroid.ui;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.text.DecimalFormat;
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

    private String salaryMinimumMaximum;
    private DecimalFormat decimalFormat;

    public JobAdapter(@NonNull Context context) {
        this.jobs = new ArrayList<>();
        this.inflater = LayoutInflater.from(context);
        this.salaryMinimumMaximum = context.getString(R.string.salary_from_minimum_to_maximum);

        this.decimalFormat = new DecimalFormat("0");
        this.decimalFormat.setGroupingSize(3);
        this.decimalFormat.setGroupingUsed(true);

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

        String jobSalary = salaryMinimumMaximum.replace("minimum", decimalFormat.format(getItem(position).getMinimum()))
                .replace("maximum", decimalFormat.format(getItem(position).getMaximum()));
        holder.jobSalary.setText( jobSalary );

        return convertView;
    }

    static class ViewHolder {

        @Bind(R.id.job_title)
        TextView jobTitle;

        @Bind(R.id.job_salary)
        TextView jobSalary;

        public ViewHolder(View v) {
            ButterKnife.bind(this, v);
        }

    }
}