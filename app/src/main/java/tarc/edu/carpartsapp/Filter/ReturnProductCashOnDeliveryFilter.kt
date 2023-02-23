package tarc.edu.carpartsapp.Filter

import android.widget.Filter
import tarc.edu.carpartsapp.Adapter.ReturnProductCashOnDeliveryAdapter
import tarc.edu.carpartsapp.Model.DeliveryModel
import tarc.edu.carpartsapp.Model.MyOrderModel

class ReturnProductCashOnDeliveryFilter: Filter {

    //arrayList in which we want to search
    private var filterList: ArrayList<DeliveryModel>

    //adapter in which filter need to be implemented
    private var returnProductCashOnDeliveryAdapter: ReturnProductCashOnDeliveryAdapter

    //constructor
    constructor(
        filterList: ArrayList<DeliveryModel>,
        returnProductCashOnDeliveryAdapter: ReturnProductCashOnDeliveryAdapter
    ) : super() {
        this.filterList = filterList
        this.returnProductCashOnDeliveryAdapter = returnProductCashOnDeliveryAdapter
    }

    override fun performFiltering(constraint: CharSequence?): Filter.FilterResults {
        var constraint = constraint
        val results = Filter.FilterResults()

        //value should not be null and not empty
        if(constraint != null && constraint.isNotEmpty()) {

            //searched value is nor null not empty

            constraint = constraint.toString()
            val filteredModels: ArrayList<DeliveryModel> = ArrayList()
            for (i in 0 until filterList.size) {
                //validate
                if (filterList[i].names.contains(constraint, ignoreCase = true) || filterList[i].orderID.contains(constraint, ignoreCase = true) || filterList[i].deliveryID.contains(constraint, ignoreCase = true)) {
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
        returnProductCashOnDeliveryAdapter.deliveryModelArrayList= results.values as ArrayList<DeliveryModel>
        //notify changes
        returnProductCashOnDeliveryAdapter.notifyDataSetChanged()
    }
}