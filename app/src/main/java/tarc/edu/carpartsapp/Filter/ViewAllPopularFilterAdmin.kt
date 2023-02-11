package tarc.edu.carpartsapp.Filter

import android.widget.Filter
import tarc.edu.carpartsapp.Adapter.ViewAllPopularAdapterAdmin
import tarc.edu.carpartsapp.Model.ViewAllPopularModel

class ViewAllPopularFilterAdmin: Filter {

    //arrayList in which we want to search
    private var filterList: ArrayList<ViewAllPopularModel>

    //adapter in which filter need to be implemented
    private var viewAllPopularAdapterAdmin: ViewAllPopularAdapterAdmin

    //constructor
    constructor(
        filterList: ArrayList<ViewAllPopularModel>,
        viewAllPopularAdapterAdmin: ViewAllPopularAdapterAdmin
    ) : super() {
        this.filterList = filterList
        this.viewAllPopularAdapterAdmin = viewAllPopularAdapterAdmin
    }

    override fun performFiltering(constraint: CharSequence?): FilterResults {
        var constraint = constraint
        val results = FilterResults()

        //value should not be null and not empty
        if(constraint != null && constraint.isNotEmpty()) {

            //searched value is nor null not empty

            constraint = constraint.toString()
            val filteredModels: ArrayList<ViewAllPopularModel> = ArrayList()
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
        viewAllPopularAdapterAdmin.viewAllPopularModelArrayList = results.values as ArrayList<ViewAllPopularModel>
        //notify changes
        viewAllPopularAdapterAdmin.notifyDataSetChanged()
    }
}