package tarc.edu.carpartsapp.Filter

import android.widget.Filter
import tarc.edu.carpartsapp.Adapter.CarPartsCategoryAdapterAdmin
import tarc.edu.carpartsapp.Model.CarPartsCategoryModel

class CarPartsCategoryFilterAdmin: Filter {

    //arrayList in which we want to search
    private var filterList: ArrayList<CarPartsCategoryModel>

    //adapter in which filter need to be implemented
    private var carPartsCategoryAdapterAdmin: CarPartsCategoryAdapterAdmin

    //constructor
    constructor(
        filterList: ArrayList<CarPartsCategoryModel>,
        carPartsCategoryAdapterAdmin: CarPartsCategoryAdapterAdmin
    ) : super() {
        this.filterList = filterList
        this.carPartsCategoryAdapterAdmin = carPartsCategoryAdapterAdmin
    }

    override fun performFiltering(constraint: CharSequence?): Filter.FilterResults {
        var constraint = constraint
        val results = Filter.FilterResults()

        //value should not be null and not empty
        if(constraint != null && constraint.isNotEmpty()) {

            //searched value is nor null not empty

            constraint = constraint.toString()
            val filteredModels: ArrayList<CarPartsCategoryModel> = ArrayList()
            for (i in 0 until filterList.size) {
                //validate
                if (filterList[i].name.contains(constraint, ignoreCase = true)) {
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
        carPartsCategoryAdapterAdmin.carPartsCategoryModelArrayList= results.values as ArrayList<CarPartsCategoryModel>
        //notify changes
        carPartsCategoryAdapterAdmin.notifyDataSetChanged()
    }
}