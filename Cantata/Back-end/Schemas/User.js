const mongoose = require('mongoose');
const { Schema } = mongoose;


const ScheduleSchema = new Schema(
    { 
        year:{
            type:Number,
            //required:true,
        },
        month:{
            type:Number,
           // required:true,
        },
        date:{
            type:Number,
           // required:true,
        },
        time:{
            type:Number,
          //  required:true,
        },
        duration:{
            type:Number,
        },
        todo:{
            type:String
        },
        location:{
            type:String
        },
        friend:{
            type:String // 친구 id
        },
        message:{
            type:String
        }
    }
);


const userdata = new Schema(
    {
        id:{
            type:String,
            required:true,
            unique: true,
        },
        pw:{
            type:String,
            required:true,
        },
        username:{
            type:String,
        },
        kakaoid:{
            type:String,
        },
        image_url:{
            type:String,
        },
        text:{
            type:String,
        },
        friends:{
            type: [], // 친구 ids
        },
        status:{
            type:String,
        },
        food:{
            type:String,
        },
        hobby:{
            type:String,
        },
        favorites:{
            type:String,
        },
        weekend:{
            type:String,
        },
        schedules: [ScheduleSchema]
        
    },
    {collection:'userdata'}
);

module.exports = mongoose.model('User', userdata);