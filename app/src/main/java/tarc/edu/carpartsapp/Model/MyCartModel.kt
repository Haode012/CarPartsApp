package tarc.edu.carpartsapp.Model
class MyCartModel{
    var id:String = ""
    var name:String = ""
    var warranty:String = ""
    var price:String = ""
    var total_quantity:String = ""
    var total_price:String = ""
    var img_url:String = ""
    var currentDate:String = ""
    var currentTime:String = ""
    var uid:String = ""

    //empty constructor, required by firebase
    constructor()

    //parameterized constructor
    constructor(
        id: String,
        name: String,
        warranty: String,
        price: String,
        total_quantity: String,
        total_price: String,
        img_url: String,
        currentDate: String,
        currentTime: String,
        uid:String

    ) {
        this.uid = uid
        this.id = id
        this.name =  name
        this.warranty =  warranty
        this.price = price
        this.total_quantity = total_quantity
        this.total_price = total_price
        this.img_url = img_url
        this.currentDate = currentDate
        this.currentTime = currentTime
    }
}



