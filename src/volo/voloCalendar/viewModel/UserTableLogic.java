package volo.voloCalendar.viewModel;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.io.Serializable;

/**
 * Created by Emin Guliyev on 06/12/2014.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class UserTableLogic implements Serializable{
    private String sortingField;
    private boolean reverse;
    private int beginIndex;
    private int maxNumber;
    private String keyword;

    public UserTableLogic() {
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
