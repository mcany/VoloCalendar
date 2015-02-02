package volo.voloCalendar.viewModel.user;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.io.Serializable;

/**
 * Created by Emin Guliyev on 06/12/2014.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class UserTable implements Serializable { //defines all parameters of filtering/sorting/paging/itemsPerPage
    private String sortingField; //sorting field
    private boolean reverse;// is sorting in reverse order
    private String keyword;// keyword for filtering
    private int currentPage;
    private int itemsPerPage;// max number of items to fetch

    public UserTable() {
    }

    public UserTable(String sortingField, boolean reverse, String keyword, int currentPage, int itemsPerPage) {
        this.sortingField = sortingField;
        this.reverse = reverse;
        this.keyword = keyword;
        this.currentPage = currentPage;
        this.itemsPerPage = itemsPerPage;
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

    public int getCurrentPage() {
        return currentPage;
    }

    public void setCurrentPage(int currentPage) {
        this.currentPage = currentPage;
    }

    public int getItemsPerPage() {
        return itemsPerPage;
    }

    public void setItemsPerPage(int itemsPerPage) {
        this.itemsPerPage = itemsPerPage;
    }

    public String getKeyword() {
        return keyword;
    }

    public void setKeyword(String keyword) {
        this.keyword = keyword;
    }
    @JsonIgnore
    public String getKeywordLike() {
        return keyword == null?"%":"%" + keyword.trim() + "%";
    }
}
