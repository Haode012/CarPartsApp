package tarc.edu.carpartsapp.Model

class CarPartsCategoryModel {
    var id:String = ""
    var name:String = ""
    var img_url:String = ""
    var type:String = ""

    //empty constructor, required by firebase
    constructor()

    //parameterized constructor
    constructor(
        id: String,
        name: String,
        img_url: String,
        type: String,
    ) {
        this.id = id
        this.name =  name
        this.img_url = img_url
        this.type = type
    }
}
