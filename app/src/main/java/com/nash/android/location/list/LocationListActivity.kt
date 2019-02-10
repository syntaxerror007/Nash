package com.nash.android.location.list

import android.app.Activity
import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatDialog
import com.nash.android.R
import com.nash.android.core.activity.CoreActivity
import com.nash.android.core.dialog.confirmation.ConfirmationDialogViewModel
import com.nash.android.core.dialog.loading.LoadingDialog
import com.nash.android.data.LocationDataModel
import com.nash.android.location.register.RegisterLocationActivity
import kotlinx.android.synthetic.main.location_list_activity.*
import org.parceler.Parcels

class LocationListActivity : CoreActivity<LocationListViewModel>() {
    private lateinit var loadingDialog: AppCompatDialog

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.location_list_activity)
        setTitle(R.string.text_location_list_title)
        setDrawerItemSelected(R.id.menu_location)
        loadingDialog = LoadingDialog(this)
        observeLiveData()
        getViewModel().getAllLocation()
        btnAddNewLocation.setOnClickListener { navigateToNewLocationForm() }
    }

    private fun observeLiveData() {
        getViewModel().getLocationListData().observe(this, Observer { observeLocation(it) })
        getViewModel().getLoadingLiveData().observe(this, Observer { observeLoading(it!!) })
    }

    private fun observeLoading(it: Boolean) {
        if (it) loadingDialog.show() else loadingDialog.dismiss()
    }

    private fun observeLocation(it: List<LocationDataModel>?) {
        recyclerViewLocation.adapter = LocationListAdapter(it ?: listOf(), {
            navigateToLocationDetail(it)
        }, {
            deleteLocation(it)
        })
    }

    private fun deleteLocation(it: LocationDataModel) {
        val confirmationDialogViewModel = ConfirmationDialogViewModel(
                "",
                getString(R.string.text_confirmation_dialog_delete_message, it.locationName),
                getString(R.string.text_common_yes),
                getString(R.string.text_common_no), {
            hideConfirmationDialog()
        }, {
            getViewModel().deleteLocation(it)
            hideConfirmationDialog()
        })
        showConfirmationDialog(confirmationDialogViewModel)
    }

    private fun navigateToLocationDetail(it: LocationDataModel) {
        val bundle = Bundle()
        bundle.putParcelable("locationDataModel", Parcels.wrap(it))
        startActivityForResult(Intent(this, RegisterLocationActivity::class.java).putExtras(bundle), 1)
    }

    private fun navigateToNewLocationForm() {
        startActivityForResult(Intent(this, RegisterLocationActivity::class.java), 1)
    }

    override fun onCreateViewModel(): LocationListViewModel {
        return ViewModelProviders.of(this).get(LocationListViewModel::class.java)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 1) {
            if (resultCode == Activity.RESULT_OK) {
                getViewModel().getAllLocation()
            }
        }
    }
}