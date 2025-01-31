package tarc.edu.carpartsapp.Model

class MyCartModel {
    var orderID:String = ""
    var id:String = ""
    var name:String = ""
    var warranty:String = ""
    var price:String = ""
    var total_quantity:String = ""
    var total_price:String = ""
    var img_url:String = ""
    var currentDate:String = ""
    var uid:String = ""
    var deliveryAddress: String = ""
    var currentTime: String = ""

    //empty constructor, required by firebase
    constructor()

    //parameterized constructor
    constructor(
        orderID: String,
        id: String,
        name: String,
        warranty: String,
        price: String,
        total_quantity: String,
        total_price: String,
        img_url: String,
        currentDate: String,
        uid:String,
        deliveryAddress: String,
        currentTime: String
    ) {
        this.orderID = orderID
        this.uid = uid
        this.id = id
        this.name =  name
        this.warranty =  warranty
        this.price = price
        this.total_quantity = total_quantity
        this.total_price = total_price
        this.img_url = img_url
        this.currentDate = currentDate
        this.deliveryAddress = deliveryAddress
        this.currentTime = currentTime
    }
}



