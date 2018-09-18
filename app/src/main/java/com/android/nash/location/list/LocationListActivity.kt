package com.android.nash.location.list

import android.app.Dialog
import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatDialog
import com.android.nash.R
import com.android.nash.core.activity.CoreActivity
import com.android.nash.core.loading_dialog.LoadingDialog
import com.android.nash.data.LocationDataModel
import com.android.nash.location.register.RegisterLocationActivity
import kotlinx.android.synthetic.main.location_list_activity.*
import org.parceler.Parcels

class LocationListActivity : CoreActivity<LocationListViewModel>() {
    private lateinit var loadingDialog : AppCompatDialog

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.location_list_activity)
        setTitle(R.string.text_location_list_title)
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
        recyclerViewLocation.adapter = LocationListAdapter(it ?: listOf(),  {
            navigateToLocationDetail(it)
        }, {
            deleteLocation(it)
        })
    }

    private fun deleteLocation(it: LocationDataModel) {
        getViewModel().deleteLocation(it)
    }

    private fun navigateToLocationDetail(it: LocationDataModel) {
        val intent = Intent(this, RegisterLocationActivity::class.java)
        val bundle = Bundle()
        bundle.putParcelable("locationDataModel", Parcels.wrap(it))
        startActivity(intent.putExtras(bundle))
    }

    private fun navigateToNewLocationForm() {
        startActivity(Intent(this, RegisterLocationActivity::class.java))
    }

    override fun onCreateViewModel(): LocationListViewModel {
        return ViewModelProviders.of(this).get(LocationListViewModel::class.java)
    }
}