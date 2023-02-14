package tarc.edu.carpartsapp.Model

class ReturnProductModel {
    var uid:String = ""
    var orderID:String = ""
    var id:String = ""
    var name:String = ""
    var warranty:String = ""
    var total_quantity:String = ""
    var img_url:String = ""
    var orderDate:String = ""
    var orderTime: String = ""
    var status: String = ""

    //empty constructor, required by firebase
    constructor()

    //parameterized constructor
    constructor(
        orderID: String,
        id: String,
        name: String,
        warranty: String,
        total_quantity: String,
        img_url: String,
        orderDate: String,
        uid:String,
        orderTime:String,
        status:String

    ) {
        this.orderID = orderID
        this.uid = uid
        this.id = id
        this.name =  name
        this.warranty =  warranty
        this.total_quantity = total_quantity
        this.img_url = img_url
        this.orderDate = orderDate
        this.orderTime = orderTime
        this.status = status
    }
}



