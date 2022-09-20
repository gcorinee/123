const mongoose = require('mongoose');
const { Schema } = mongoose;

const RequestSchema = new Schema(
    { 
        sender_id:{
            type:String,
        },
        sender_name:{
            type:String,
        },
        sender_image:{
            type:String,
        },
        receiver:{
            type:String, // friend id의 배열
        },
        receiver_name:{
            type:String,
        },
        receiver_image:{
            type:String,
        },
        year:{
            type:Number,
        },
        month:{
            type:Number,
        },
        date:{
            type:Number,
        },
        time:{
            type:Number,
        },
        duration:{
            type:Number
        },
        todo:{
            type:String
        },
        location:{
            type:String
        },
        message:{
            type:String
        },
        accepts:{
            type : Boolean
        }
    },
    {collection:'requestdata'}
);

module.exports = mongoose.model('Request', RequestSchema);