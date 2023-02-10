package tarc.edu.carpartsapp.Filter

import android.widget.Filter
import tarc.edu.carpartsapp.Adapter.RecommendedAdapterCustomer
import tarc.edu.carpartsapp.Model.RecommendedModel

class RecommendedFilterCustomer: Filter {

    //arrayList in which we want to search
    private var filterList: ArrayList<RecommendedModel>

    //adapter in which filter need to be implemented
    private var recommendedAdapterCustomer: RecommendedAdapterCustomer

    //constructor
    constructor(
        filterList: ArrayList<RecommendedModel>,
        recommendedAdapterCustomer: RecommendedAdapterCustomer
    ) : super() {
        this.filterList = filterList
        this.recommendedAdapterCustomer = recommendedAdapterCustomer
    }

    override fun performFiltering(constraint: CharSequence?): FilterResults {
        var constraint = constraint
        val results = FilterResults()

        //value should not be null and not empty
        if(constraint != null && constraint.isNotEmpty()) {

            //searched value is nor null not empty

            constraint = constraint.toString()
            val filteredModels: ArrayList<RecommendedModel> = ArrayList()
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
        recommendedAdapterCustomer.recommendedModelArrayList= results.values as ArrayList<RecommendedModel>
        //notify changes
        recommendedAdapterCustomer.notifyDataSetChanged()
    }
}