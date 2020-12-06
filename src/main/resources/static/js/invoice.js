$(function () {
    $("#amount-lower").change(() => {
        let upper = moneyToCapital($("#amount-lower").val());
        console.info("upper: " + upper);
        $("#amount-upper").val(upper);
    })
});


function ajaxSubmit() {
    let data = getObject();
    let cr = check(data);
    if (cr) {
        alert(cr);
    } else {
        $.ajax({
            url: 'save',
            type: 'post',
            dataType: 'json',
            contentType: 'application/json',
            data: JSON.stringify(data),
            async: true,
            success: (data) => {
                if (data.status === 1) {
                    alert(data.msg);
                } else {
                    alert("生成发票成功!\n请在以下路径查看:\n" + data.data.url);
                }
            }
        });
    }
    return false;
}

function getObject() {
    let invoice = {};

    let payer = {};
    payer.name = $("#payer-name").val();
    payer.account = $("#payer-account").val();
    payer.bank = $("#payer-bank").val();

    let payee = {};
    payee.name = $("#payee-name").val();
    payee.account = $("#payee-account").val();
    payee.bank = $("#payee-bank").val();

    let items = [];
    for (let i = 0; i < 3; i++) {
        let select1 = $("#item-select-" + i);
        if (select1.val()) {
            let item = {};
            item.value = select1.val();
            item.code = $("#item-code-" + i).val();
            item.num = $("#item-num-" + i).val();
            item.norm = $("#item-norm-" + i).val();
            item.amount = $("#item-amount-" + i).val() || 0;
            items.push(item);
        }
    }

    invoice.payer = payer;
    invoice.payee = payee
    invoice.items = items;
    invoice.amountUpper = $("#amount-upper").val();
    invoice.amountLower = $("#amount-lower").val() || 0;
    return invoice;
}

function check(invoice) {
    if (!!!invoice.payer.name) return "付款人全称不能为空!";
    if (!!!invoice.payer.account) return "付款人账号不能为空!";
    if (!!!invoice.payer.bank) return "付款人开户银行不能为空!";

    if (!!!invoice.payee.name) return "收款人全称不能为空!";
    if (!!!invoice.payee.account) return "收款人账号不能为空!";
    if (!!!invoice.payee.bank) return "收款人开户银行不能为空!";

    if (invoice.items.length === 0) return "请选择收入项目!";
    for (let i = 0; i < invoice.items.length; i ++ ) {
        if (!!!invoice.items[i].value) return "收入项目";
        if (!!!invoice.items[i].code) return "编码";
        if (!!!invoice.items[i].num) return "数量";
        if (!!!invoice.items[i].norm) return "收缴标准";
        if (!!!invoice.items[i].amount) return "金额";
    }

    if (!!!invoice.amountUpper) return "金额(大写)不能为空!";
    if (!!!invoice.amountLower) return "金额(小写)不能是负数!";

    return "";
}

//数字转大写
function moneyToCapital(n) {
    if (n === 0) {
        return "零";
    }
    if (!/^(0|[1-9]\d*)(\.\d+)?$/.test(n))
        return "";
    let unit = "千百拾亿千百拾万千百拾元角分", str = "";
    n += "00";
    let p = n.indexOf('.');
    if (p >= 0)
        n = n.substring(0, p) + n.substr(p + 1, 2);
    unit = unit.substr(unit.length - n.length);
    for (let i = 0; i < n.length; i++)
        str += '零壹贰叁肆伍陆柒捌玖'.charAt(n.charAt(i)) + unit.charAt(i);
    return str.replace(/零(千|百|拾|角)/g, "零").replace(/(零)+/g, "零").replace(/零(万|亿|元)/g, "$1").replace(/(亿)万|壹(拾)/g, "$1$2").replace(/^元零?|零分/g, "").replace(/元$/g, "元整");
}