package tarc.edu.carpartsapp.Filter

import android.widget.Filter
import tarc.edu.carpartsapp.Adapter.ViewAllRecommendedAdapterAdmin
import tarc.edu.carpartsapp.Model.ViewAllRecommendedModel


class ViewAllRecommendedFilterAdmin: Filter {

    //arrayList in which we want to search
    private var filterList: ArrayList<ViewAllRecommendedModel>

    //adapter in which filter need to be implemented
    private var viewAllRecommendedAdapterAdmin: ViewAllRecommendedAdapterAdmin

    //constructor
    constructor(
        filterList: ArrayList<ViewAllRecommendedModel>,
        viewAllRecommendedAdapterAdmin: ViewAllRecommendedAdapterAdmin
    ) : super() {
        this.filterList = filterList
        this.viewAllRecommendedAdapterAdmin = viewAllRecommendedAdapterAdmin
    }

    override fun performFiltering(constraint: CharSequence?): FilterResults {
        var constraint = constraint
        val results = FilterResults()

        //value should not be null and not empty
        if(constraint != null && constraint.isNotEmpty()) {

            //searched value is nor null not empty

            constraint = constraint.toString()
            val filteredModels: ArrayList<ViewAllRecommendedModel> = ArrayList()
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
        viewAllRecommendedAdapterAdmin.viewAllRecommendedModelArrayList = results.values as ArrayList<ViewAllRecommendedModel>
        //notify changes
        viewAllRecommendedAdapterAdmin.notifyDataSetChanged()
    }
}