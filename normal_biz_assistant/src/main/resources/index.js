var raw = $('.ant-table-tbody')[0].innerText;
if (raw) {
    var items = raw.split('\n').filter(function(item){
        return  item.trim();
    }).map(function(item){
        return item.trim()
    })

    var order = {}
    order.createDateTime = items[0] + items[1];
    order.goodType = items[2]
    order.sellerOrderId = items[3]
    order.aliOrderNo = items[4];
    order.buyerPhone = items[5];
    order.buyerName = items[6];
    order.totalPrice = items[7]
    order.refundPrice = items[8];
    order.servicePrice = items[9];
    order.payStatus = items[10];
    console.log(order);
}




