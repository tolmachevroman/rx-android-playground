package sample.rxandroid.ui;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Locale;

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
    private String salaryUnspecified;
    private DecimalFormat decimalFormat;
    private String perYear;
    private String perMonth;
    private String perHour;

    private String openDates;
    private String openDatesUnspecified;
    private SimpleDateFormat dateFormat;

    public JobAdapter(@NonNull Context context) {
        this.jobs = new ArrayList<>();
        this.inflater = LayoutInflater.from(context);
        this.salaryMinimumMaximum = context.getString(R.string.salary_from_minimum_to_maximum);
        this.salaryUnspecified = context.getString(R.string.salary_unspecified);
        this.perYear = context.getString(R.string.per_year);
        this.perMonth = context.getString(R.string.per_month);
        this.perHour = context.getString(R.string.per_hour);

        this.decimalFormat = new DecimalFormat("0");
        this.decimalFormat.setGroupingSize(3);
        this.decimalFormat.setGroupingUsed(true);

        this.openDates = context.getString(R.string.open_from_date_to_date);
        this.openDatesUnspecified = context.getString(R.string.open_dates_unspecified);
        this.dateFormat = new SimpleDateFormat("dd MMM", Locale.US);
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


        /**
         * Show job title
         */
        String jobTitle = getItem(position).getPositionTitle();
        holder.jobTitle.setText(jobTitle.substring(0, 1).toUpperCase() + jobTitle.substring(1, jobTitle.length()).toLowerCase());


        /**
         * Show job salary
         */
        if (getItem(position).getMinimum() != null && getItem(position).getMaximum() != null) {

            String jobSalary = salaryMinimumMaximum.replace("minimum", decimalFormat.format(getItem(position).getMinimum()))
                    .replace("maximum", decimalFormat.format(getItem(position).getMaximum()));

            switch (getItem(position).getRateIntervalCode()) {
                case PA:
                    jobSalary = jobSalary.concat(" " + perYear);
                    break;
                case PM:
                    jobSalary = jobSalary.concat(" " + perMonth);
                    break;
                case PH:
                    jobSalary = jobSalary.concat(" " + perHour);
                    break;
            }

            holder.jobSalary.setText(jobSalary);

        } else {
            holder.jobSalary.setText(salaryUnspecified);
        }


        /**
         * Show job start and end dates
         */
        if (getItem(position).getStartDate() != null && getItem(position).getEndDate() != null) {

            String jobOpenDates = openDates.replace("startDate", dateFormat.format(getItem(position).getStartDate())).
                    replace("endDate", dateFormat.format(getItem(position).getEndDate()));
            holder.jobOpenDates.setText(jobOpenDates);

        } else {
            holder.jobOpenDates.setText(openDatesUnspecified);
        }


        return convertView;
    }

    static class ViewHolder {

        @Bind(R.id.job_title)
        TextView jobTitle;

        @Bind(R.id.job_salary)
        TextView jobSalary;

        @Bind(R.id.job_open_dates)
        TextView jobOpenDates;

        public ViewHolder(View v) {
            ButterKnife.bind(this, v);
        }

    }
}