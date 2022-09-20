const { Long } = require('mongodb');
const mongoose = require('mongoose');
const { Schema } = mongoose;


const CommentSchema = new Schema(
    {
        id:{
            type:String
        },
        content:{
            type:String
        },
        vote:{
            type:Number
        }
    }
)

const BoardSchema = new Schema(
    {
        id:{
            type:String
        },
        username:{
            type:String
        },
        title:{
            type:String
        },
        content:{
            type:String
        },
        tag:{
            type:[]
        },
        votes:{
            type:Number
        },
        views:{
            type:Number
        },
        comments:{ // 댓글
            type:[CommentSchema]
        }
    },{collection:'boarddata'}
);

module.exports = mongoose.model('Board', BoardSchema);