package tarc.edu.carpartsapp.Filter

import android.widget.Filter
import tarc.edu.carpartsapp.Adapter.ReturnProductAdminAdapter
import tarc.edu.carpartsapp.Adapter.ReturnProductCashOnDeliveryAdapter
import tarc.edu.carpartsapp.Model.MyOrderModel
import tarc.edu.carpartsapp.Model.ReturnProductModel

class ReturnProductAdminFilter: Filter {

    //arrayList in which we want to search
    private var filterList: ArrayList<ReturnProductModel>

    //adapter in which filter need to be implemented
    private var returnProductAdminAdapter: ReturnProductAdminAdapter

    //constructor
    constructor(
        filterList: ArrayList<ReturnProductModel>,
        returnProductAdminAdapter: ReturnProductAdminAdapter
    ) : super() {
        this.filterList = filterList
        this.returnProductAdminAdapter = returnProductAdminAdapter
    }

    override fun performFiltering(constraint: CharSequence?): Filter.FilterResults {
        var constraint = constraint
        val results = Filter.FilterResults()

        //value should not be null and not empty
        if(constraint != null && constraint.isNotEmpty()) {

            //searched value is nor null not empty

            constraint = constraint.toString()
            val filteredModels: ArrayList<ReturnProductModel> = ArrayList()
            for (i in 0 until filterList.size) {
                //validate
                if (filterList[i].uid.contains(constraint, ignoreCase = true) || filterList[i].name.contains(constraint, ignoreCase = true)) {
                    //add to filtered list
                    filteredModels.add(filterList[i])
                }
            }
            results.count = filteredModels.size
            results.values = filteredModels
        }
        else{
            //search value is either null or empty
            results.count = filterList.size
            results.values = filterList
        }
        return results //don't miss it
    }

    override fun publishResults(constraint: CharSequence?, results: Filter.FilterResults) {
        //apply filter changes
        returnProductAdminAdapter.returnProductModelArrayList= results.values as ArrayList<ReturnProductModel>
        //notify changes
        returnProductAdminAdapter.notifyDataSetChanged()
    }
}