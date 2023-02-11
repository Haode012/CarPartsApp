package tarc.edu.carpartsapp.Model

class MyOrderModel {
    var paymentID:String = ""
    var bankType:String = ""
    var cardNumber:String = ""
    var expirationDateMonth:String = ""
    var expirationDateYear:String = ""
    var CVV:String = ""
    var orderID:String = ""
    var id:String = ""
    var name:String = ""
    var warranty:String = ""
    var price:String = ""
    var total_quantity:String = ""
    var total_price:String = ""
    var img_url:String = ""
    var currentDate:String = ""
    var deliveryAddress: String = ""
    var uid:String = ""
    var currentTime: String = ""

    //empty constructor, required by firebase
    constructor()

    //parameterized constructor
    constructor(
        paymentID: String,
        bankType:String,
        cardNumber: String,
        expirationDateMonth:String,
        expirationDateYear:String,
        CVV:String,
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
        currentTime:String,
        deliveryAddress: String

    ) {
        this.paymentID = paymentID
        this.bankType = bankType
        this.cardNumber =  cardNumber
        this.expirationDateMonth = expirationDateMonth
        this.expirationDateYear =  expirationDateYear
        this.CVV = CVV
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
        this.currentTime = currentTime
        this.deliveryAddress = deliveryAddress
    }
}



