package volo.voloCalendar.viewModel;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.io.Serializable;

/**
 * Created by Emin Guliyev on 06/12/2014.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class UserTableViewModel implements Serializable { //defines all parameters of filtering/sorting/paging/itemsPerPage
    private String sortingField; //sorting field
    private boolean reverse;// is sorting in reverse order
    private String keyword;// keyword for filtering
    private int beginIndex;// begin index after soring and filtering applied
    private int maxNumber;// max number of items to fetch

    public UserTableViewModel() {
    }

    public String getSortingField() {
        return sortingField;
    }

    public void setSortingField(String sortingField) {
        this.sortingField = sortingField;
    }

    public boolean isReverse() {
        return reverse;
    }

    public void setReverse(boolean isReverse) {
        this.reverse = isReverse;
    }

    public int getBeginIndex() {
        return beginIndex;
    }

    public void setBeginIndex(int beginIndex) {
        this.beginIndex = beginIndex;
    }

    public int getMaxNumber() {
        return maxNumber;
    }

    public void setMaxNumber(int maxNumber) {
        this.maxNumber = maxNumber;
    }

    public String getKeyword() {
        return keyword;
    }

    public void setKeyword(String keyword) {
        this.keyword = keyword;
    }
}
