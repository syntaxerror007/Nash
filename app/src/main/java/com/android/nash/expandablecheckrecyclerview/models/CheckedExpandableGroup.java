package com.android.nash.expandablecheckrecyclerview.models;

import android.os.Parcel;

import com.android.nash.expandablerecyclerview.models.ExpandableGroup;
import com.google.firebase.database.Exclude;

import java.util.Arrays;
import java.util.List;

import kotlin.jvm.Transient;

/**
 * An extension of ExpandableGroup that holds onto the checked state of it's children
 */
public abstract class CheckedExpandableGroup extends ExpandableGroup {

    @Exclude
    public boolean[] selectedChildren;

    public CheckedExpandableGroup(String title, List items) {
        super(title, items);
        selectedChildren = new boolean[items.size()];
        for (int i = 0; i < items.size(); i++) {
            selectedChildren[i] = false;
        }
    }

    public void checkChild(int childIndex) {
        if (childIndex < selectedChildren.length)
            selectedChildren[childIndex] = true;
    }

    public void unCheckChild(int childIndex) {
        selectedChildren[childIndex] = false;
    }

    public boolean isChildChecked(int childIndex) {
        if (childIndex >= selectedChildren.length)
            return false;
        return selectedChildren[childIndex];
    }

    public void clearSelections() {
        if (selectedChildren != null) {
            Arrays.fill(selectedChildren, false);
        }
    }

    protected CheckedExpandableGroup(Parcel in) {
        super(in);
        selectedChildren = in.createBooleanArray();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        super.writeToParcel(dest, flags);
        dest.writeBooleanArray(selectedChildren);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public abstract void onChildClicked(int childIndex, boolean checked);

}
