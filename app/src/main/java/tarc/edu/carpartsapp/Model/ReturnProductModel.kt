package tarc.edu.carpartsapp.Model

class ReturnProductModel {
    var uid:String = ""
    var orderID:String = ""
    var id:String = ""
    var name:String = ""
    var warranty:String = ""
    var total_quantity:String = ""
    var img_url:String = ""
    var deliveryID: String = ""
    var deliveryAddress: String = ""
    var deliveredDate:String = ""
    var deliveredTime: String = ""
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
        deliveredDate: String,
        uid:String,
        deliveredTime: String,
        status:String,
        deliveryID: String,
        deliveryAddress: String
    ) {
        this.orderID = orderID
        this.uid = uid
        this.id = id
        this.name =  name
        this.warranty =  warranty
        this.total_quantity = total_quantity
        this.img_url = img_url
        this.deliveredDate = deliveredDate
        this.deliveredTime = deliveredTime
        this.status = status
        this.deliveryID = deliveryID
        this.deliveryAddress = deliveryAddress
    }
}



