package tarc.edu.carpartsapp.Model

class RecommendedModel {
    var id:String = ""
    var name:String = ""
    var description:String = ""
    var price:String = ""
    var img_url:String = ""

    //empty constructor, required by firebase
    constructor()

    //parameterized constructor
    constructor(
        id: String,
        name: String,
        description: String,
        price: String,
        img_url: String,
    ) {
        this.id = id
        this.name =  name
        this.description =  description
        this.price = price
        this.img_url = img_url
    }
}


