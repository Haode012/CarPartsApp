package tarc.edu.carpartsapp.Model

class ViewAllRecommendedModel{
    var id:String = ""
    var name:String = ""
    var description:String = ""
    var price:String = ""
    var warranty:String = ""
    var img_url:String = ""
    var type:String = ""

    //empty constructor, required by firebase
    constructor()

    //parameterized constructor
    constructor(
        id: String,
        name: String,
        description: String,
        price: String,
        warranty: String,
        img_url: String,
        type: String
    ) {
        this.id = id
        this.name =  name
        this.description =  description
        this.price = price
        this.warranty = warranty
        this.img_url = img_url
        this.type = type
    }
}
