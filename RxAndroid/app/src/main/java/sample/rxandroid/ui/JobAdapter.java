package sample.rxandroid.ui;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;
import rx.Observer;
import sample.rxandroid.R;
import sample.rxandroid.network.Job;

/**
 * Created by romantolmachev on 7/10/15.
 */
public class JobAdapter extends BaseAdapter implements Observer<Job> {

    private ArrayList<Job> jobs;

    private final LayoutInflater inflater;

    public JobAdapter(@NonNull Context context) {
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

    @Override
    public void onCompleted() {

    }

    @Override
    public void onError(Throwable e) {

    }

    @Override
    public void onNext(Job job) {
        System.out.println("onNext Thread: " + Thread.currentThread());
        System.out.println("Adding job to JobAdapter: " + job.getPositionTitle());
        this.jobs.add(job);



        notifyDataSetChanged();
    }

    public void clearJobs() {
        this.jobs = new ArrayList<>();
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

        holder.jobTitle.setText(getItem(position).getPositionTitle());

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