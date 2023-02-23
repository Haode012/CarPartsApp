package tarc.edu.carpartsapp.Model

class DeliveryModel {
    var TotalAmount: String=""
    var address: String=""
    var deliveryID: String =""
    var id: String=""
    var img_url: String=""
    var names: String=""
    var orderID: String=""
    var userId: String=""
    var quantity: String=""
    var deliveredDate: String=""
    var deliveredTime: String=""
    var warranty: String=""

    constructor()

    constructor(
        TotalAmount: String,
        address: String,
        deliveryID: String,
        id: String,
        img_url: String,
        names: String,
        orderID: String,
        userId:String,
        quantity: String,
        deliveredDate: String,
        deliveredTime: String,
        warranty: String
    ) {
        this.TotalAmount = TotalAmount
        this.address = address
        this.deliveryID = deliveryID
        this.id = id
        this.img_url = img_url
        this.names = names
        this.orderID = orderID
        this.userId = userId
        this.quantity = quantity
        this.deliveredDate = deliveredDate
        this.deliveredTime = deliveredTime
        this.warranty = warranty

    }
}