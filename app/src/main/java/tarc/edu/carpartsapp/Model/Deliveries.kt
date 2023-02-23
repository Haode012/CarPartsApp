package tarc.edu.carpartsapp.Model

class Deliveries {
    var orderID: String=""
    var address: String=""
    var totalAmount: String=""
    var userId: String=""
    var img_url: String=""
    var name: String=""
    var quantity: String=""
    var id: String=""

    constructor()

    constructor(
        orderID: String,
        id: String,
        name: String,
        quantity: String,
        totalAmount: String,
        img_url: String,
        userId:String,
        address: String,
    ) {
        this.orderID = orderID
        this.address = address
        this.totalAmount = totalAmount
        this.userId = userId
        this.img_url = img_url
        this.name = name
        this.quantity = quantity
        this.id = id

    }
}