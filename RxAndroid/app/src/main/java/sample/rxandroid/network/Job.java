package sample.rxandroid.network;

import com.google.gson.annotations.SerializedName;

import java.util.Date;
import java.util.List;

/**
 * Created by romantolmachev on 25/9/15.
 */
public class Job {

    @SerializedName("id")
    String id;

    @SerializedName("position_title")
    String positionTitle;

    @SerializedName("organization_name")
    String organizationName;

    @SerializedName("rate_interval_code")
    String rateIntervalCode;

    @SerializedName("minimum")
    int minimum;

    @SerializedName("maximum")
    int maximum;

    @SerializedName("start_date")
    Date startDate;

    @SerializedName("end_date")
    Date endDate;

    @SerializedName("locations")
    List<String> locations;

    @SerializedName("url")
    String url;

    public String getId() {
        return id;
    }

    public String getPositionTitle() {
        return positionTitle;
    }

    public String getOrganizationName() {
        return organizationName;
    }

    public String getRateIntervalCode() {
        return rateIntervalCode;
    }

    public int getMinimum() {
        return minimum;
    }

    public int getMaximum() {
        return maximum;
    }

    public Date getStartDate() {
        return startDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public List<String> getLocations() {
        return locations;
    }

    public String getUrl() {
        return url;
    }


}
