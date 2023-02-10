package tarc.edu.carpartsapp.Filter

import android.widget.Filter
import tarc.edu.carpartsapp.Adapter.PopularAdapterCustomer
import tarc.edu.carpartsapp.Model.PopularModel

class PopularFilterCustomer: Filter {

    //arrayList in which we want to search
    private var filterList: ArrayList<PopularModel>

    //adapter in which filter need to be implemented
    private var popularAdapterCustomer: PopularAdapterCustomer

    //constructor
    constructor(
        filterList: ArrayList<PopularModel>,
        popularAdapterCustomer: PopularAdapterCustomer
    ) : super() {
        this.filterList = filterList
        this.popularAdapterCustomer = popularAdapterCustomer
    }

    override fun performFiltering(constraint: CharSequence?): FilterResults {
        var constraint = constraint
        val results = FilterResults()

        //value should not be null and not empty
        if(constraint != null && constraint.isNotEmpty()) {

            //searched value is nor null not empty

            constraint = constraint.toString()
            val filteredModels: ArrayList<PopularModel> = ArrayList()
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

    override fun publishResults(constraint: CharSequence?, results: FilterResults) {
        //apply filter changes
        popularAdapterCustomer.popularModelArrayList = results.values as ArrayList<PopularModel>
        //notify changes
        popularAdapterCustomer.notifyDataSetChanged()
    }
}