const express = require('express');
const multer  = require('multer');
const path = require('path');
const mongoose = require('mongoose'); //mongoose를 import
var bodyParser = require('body-parser');

const User = require('./Schemas/User');
const Board = require('./Schemas/Board');
const Request = require('./Schemas/Request');
var ObjectId = require('mongoose').Types.ObjectId;

const app = express();
app.use(express.json());
const server = require('http').createServer(app);
const io = require('socket.io')(server);

var url = require('url');
var fs = require('fs');
var mime = require('mime');

const upload = multer({ // for image
    storage: multer.diskStorage({
        destination(req, file, cb) {
            cb(null, 'images/');
        },
        filename(req, file, cb) {
            const ext = path.extname(file.originalname);
            cb(null, path.basename(file.originalname, ext) + Date.now() + ext);
        }
    }),
    limits: { fileSize: 5 * 1024 * 1024 },
});

/*
io.on('connection',socket=>{
    console.log("SOCKET CONNECTED");
    socket.on("request", (data) =>{
        hold = JSON.parse(data);
        console.log(hold);
        console.log(hold.date);
        console.log(hold.month);
        console.log(hold.senderId);
    });
});
*/


server.listen(80,(err)=>{
    if(err){
        return console.log("########################### ERROR #################################");
    }else{
        //console.log(`Server running on port ${PORT}`);
        mongoose.connect(
            "mongodb://127.0.0.1:27017/test",
            {useNewUrlParser: true},
            err=>{
                if(err){
                    console.log("CONNECTION ERROR");
                    console.log(err);
                }else{
                    console.log("Connected to database successfully");
                }
            }
        )
    }
}) // 
app.get('/',(req,res)=>{
    res.json({HELLOd:"HELsLO"});
}); // for test



app.get('/user/friends/:id',[],(req,res)=>{  // input: _id  output: friends   Get User Friends
    console.log("################ getUserFriends: " + req.params.id);
    var hold = []
    User.find({_id:{$ne:req.params.id}},['_id'],(err,result)=>{if(err){console.log(err);}else{if(result==null){console.log("NULL!!");}else{ // {_id:{$ne:req.params.id}}
        console.log("result: "+result);
        for(index in result){
            hold.push(result[index]._id);
        }
    }}});
    User.findById(req.params.id,(err,result)=>{
        if(err){
            res.status(400).send();
        }else{
            if(result==null){
                console.log("NO INFO");
                res.json({"ResponseCode":400});
            }else{
                console.log("getFriends Success");
                console.log(hold);
                res.json(hold);
            }
        }
    });
});
app.get('/user/:id',(req,res)=>{  // input: _id  output: user   getUserById
    console.log("####################### getUserById: " + req.params.id);

    User.findById(req.params.id,['username','image_url','status','food','hobby','favorites','weekend','_id'],(err,result)=>{//,['id','username','imgUrl','status','food','hobby','favorites','weekend','-_id']
        if(err){                   
            res.status(400).send();
        }else{
            if(result==null){
                console.log("NO INFO");
                res.json({"ResponseCode":400});
            }else{
                /*
                hold = JSON.parse(JSON.stringify(result)); // 깊은 복사
                hold._id = result._id.toString();
                console.log(hold);
                res.json(hold);
                */
               //깊은 복사
               console.log(result);
               res.json(result)
            }
        }
    });
});
app.post('/user/update',(req,res)=>{ // 내 user 정보를 업데이트
    console.log("############## USER INFO UPDATE #################");
    User.findById(req.body._id,(err,result)=>{if(err){console.log(err);}else{if(result==null){console.log("NULL!!");}else{
        result.username = req.body.username;
        result.image_url = req.body.image_url;
        result.status = req.body.status;
        result.food = req.body.food;
        result.favorites = req.body.favorites;
        result.hobby = req.body.hobby;
        result.weekend = req.body.weekend;
        result.save();
        res.json({"ResponeseCode":200});
    }}});
});
app.get('/user/friends/profile/:id',(req,res)=>{  // input: _id  output: id, username,imgurl   getUserById
    console.log("####################### getUserById: " + req.params.id);

    User.find({_id: req.params.id},['id','username','imgUrl','-_id'],(err,result)=>{//,['id','username','imgUrl','status','food','hobby','favorites','weekend','-_id']
        if(err){
            res.status(400).send();
        }else{
            if(result==null){
                console.log("NO INFO");
                res.json({"ResponseCode":400});
            }else{
                /*
                hold = JSON.parse(JSON.stringify(result)); // 깊은 복사
                hold._id = result._id.toString();
                console.log(hold);
                res.json(hold);
                */
               //깊은 복사
               console.log(result[0]);
               res.json(result[0])
            }
        }
    });
});
app.post('/user/signup',(req,res)=>{ // Signup,  id - pw - username
    console.log("###################### SIGN UP #######################");
    console.log(req.body);

    User.exists({id:req.body.id},(err,result)=>{
        if(err){
            res.status(400).send();
        }else{
            if(result==null){
                console.log("NOT EXIST, DO CREATE");
                User.create(req.body,(err,result)=>{ // 새로운 User 생길 시 전체 User 테이블 업데이트
                    console.log(result);
                });
                res.json({"ResponeseCode":200});
            }else{
                console.log("Already EXIST!!");
                //console.log("### _____ID: "+req.body._id.toString());
                res.json({"ResponseCode":400});
            }
        }
    });
});
app.post('/user/login',(req,res)=>{ // LogIn
    console.log("################### LOG IN ##################")
    var _id= req.body.id;
    var _pw = req.body.pw;
    console.log("ID: "+_id+" PW: "+_pw);

    User.findOne({id:_id, pw:_pw},(err,result)=>{
        if(err){
            res.status(400).send();
        }else{
            if(result==null){
                console.log("INCORRECT ID or PW");
                res.json({"ResponseCode":400});
            }else{
                console.log("LOGIN SUCCESS");
                console.log(result);
                console.log("_ID: "+result._id.toString()); //  _id 확인 가능
                console.log("TYPE is "+typeof(result._id.toString()));
                res.json({"id":result._id.toString()});
                
            }
        }
    })
});
app.get('/user/schedule/:id/:year/:month',(req,res)=>{ // id, year, month로 해당 월 calendar 가져옴
    console.log("################### GET_USER_SCHEDULE_Month ###################");
    
    User.findById(req.params.id,['schedules','-_id'],(err,result)=>{ // _id는 또 생기므로 id로 사용해야함 -> 로컬/ 폰으로 할때마다 바꿔줘야함 주의!
        if(err){
            console.log("EERROO");
            console.log(err);
            res.status(400).send();
        }else{
            if(result==null){
                console.log("NO INFO");
                res.json({"ResponseCode":400});
            }else{
                const schedule_array = [];
                console.log(result);
                for(index in result.schedules){
                    if((result.schedules[index].year == req.params.year) && (result.schedules[index].month == req.params.month))
                    schedule_array.push(result.schedules[index]);
                }
                console.log("RETURN Monthly SCHEDULES");
                console.log(schedule_array);
                res.json(schedule_array);
            }
        }
    });
});
app.get('/user/schedule/date/:id/:year/:month/:date',(req,res)=>{ // GET_USER_SCHEDULE_Date
    console.log("################## GET_USER_SCHEDULE_DATE #################");
    
    User.findById(req.params.id,['schedules','-_id'],(err,result)=>{ // _id는 또 생기므로 id로 사용해야함 -> 로컬/ 폰으로 할때마다 바꿔줘야함 주의!
        // find하면 다 가져오나?
        if(err){
            res.status(400).send();
        }else{
            if(result==null){
                console.log("NO INFO");
                res.json({"ResponseCode":400});
            }else{
                var schedule_array = [];
                for(index in result.schedules){
                    if((result.schedules[index].year == req.params.year) && (result.schedules[index].month == req.params.month)){
                        if(result.schedules[index].date == req.params.date){
                            console.log("값 "+result.schedules[index].date)
                            console.log("파라미터 " + req.params.date);
                            schedule_array.push(result.schedules[index]);
                        }
                    }
                }
                console.log("RETURN Date SCHEDULES");
                console.log(schedule_array);
                res.json(schedule_array);
            }
        }
    });
});


app.get('/board',(req,res)=>{ // 게시판 전체 조회
    console.log("###############  BOARD  ################");
    
    query = {};
    output = [''];
    Board.find(query,output,(err,result)=>{
        if(err){
            console.log("err");
        }else{
            if(result==null){
                console.log("FIND NULL");
            }else{
                console.log(result);
                res.json(result);
            }
        }
    });
});
app.post('/board/write',(req,res)=>{ // 게시판 글쓰기
    Board.create({
        "id":req.body.id,
        "username":req.body.username,
        "title":req.body.title,
        "content":req.body.content,
        "tag":req.body.tag,
        "votes": 0,
        "views": 0,
        "comments":[]
    },(err,result)=>{
        if(err){console.log(err);res.json({"ResponseCode":200});}else{
            res.json({"ResponseCode":200});
        }
    });
});
app.post('/board/delete',(req,res)=>{ // 게시물 id, 회원 id로 게시물 삭제
    console.log("################ DELETE #################");
    query = {_id:req.body._id, id:req.body.id};
    console.log("##### "+req.params.id+"  "+req.params._id);
    Board.deleteOne(query,(err,result)=>{
        if(err){
            console.log(err);
            res.json({"ResponseCode":400});
        }else{
            console.log(result);
            res.json({"ResponseCode":200});
        }
    });
});
app.post('/board/comment',(req,res)=>{ // 게시물 id(_id), 회원 id(id)를 통해 댓글 작성
    console.log("################# COMMENT #################");
    comment = {
        id:req.body.id,
        content:req.body.content,
        vote:0
    };
    query = {_id:req.body._id, id:req.body.id};
    update = {$push:{comments:comment}};
    Board.findOneAndUpdate(query,update,(err,result)=>{
    if(err){
        console.log(err);
        res.json({"ResponseCode":400});
    }else{ 
        if(result==null){
            console.log("게시물 없음");
            res.json({"ResponseCode":400});
        }else{
            console.log("댓글 작성 성공");
            res.json({"ResponseCode":200});
        }
    }
   });
});
app.post('/board/comment/delete',(req,res)=>{ // 게시물 id(id1), 댓글 id(id2), 회원 id(id3)를 통해 댓글 삭제
    console.log("\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n");
    console.log("################ COMMENT DELETE #################");
    
    find = {_id:req.body.id1}; // 게시물 탐색
    Board.findOne(find,(err,result)=>{ // ['comments','-_id']
        if(err){
            console.log(err);
        }else{
            if(result==null){
                console.log("NULL");
            }else{
                var hold = result;
                for(index in result.comments){
                    console.log("###### index"+index);
                    if(result.comments[index]._id == req.body.id2){ // 해당 댓글 발견   -  여기서 안잡히고 그냥 넘어가짐
                        console.log(" ######## INDEX FOUND ########" + index);
                        if(result.comments[index].id == req.body.id3){ // 해당 댓글의 id가 내 id면
                            result.comments.splice(index,1); // 삭제
                            break;
                        }
                    }
                }
                result.save();
                res.json({"ResponseCode":200});
            }
        }
    });  
});
app.get('/board/:id',(req,res)=>{ // 게시물 id로 게시물 보기
    console.log("########### 게시물 조회 ############");
    find = {_id:req.params.id};
    update = {$inc:{views:1}}
    Board.findOneAndUpdate(find,update,(err,result)=>{
    if(err){
        console.log(err);
    }else{
        if(result==null){
            console.log("NI::");
        }else{
            console.log(result);
            res.json(result);
        }
    }
   });
});


app.post('/request', (req,res)=>{  // id, schedule로 request보냄, Request DB에 저장
    console.log("############# REQUEST 보내기 #############");
    var a = true;
    var b = true;
    output = ['schedules','-_id'];
    User.findById(req.body._id,output,(err,result)=>{
        if(err){console.log(err)}else{if(result==null){console.log("NULL!!")}else{
            console.log("############# 내 스케줄 중복 확인");
            console.log(result);
            console.log(result.schedules);
            console.log("길이: "+result.schedules.length);

            if(result.schedules.length == 0){
                console.log("요청 일정: "+req.body.year + " "+req.body.month + " "+req.body.date +" "+ req.body.time+ " "+req.body.duration);
            }else{
                console.log("요청 일정: "+req.body.year + " "+req.body.month + " "+req.body.date +" "+ req.body.time+ " "+req.body.duration);
                for(index in result.schedules){
                    console.log("내  일정: "+result.schedules[index].year + " "+result.schedules[index].month + " "+result.schedules[index].date +" "+ result.schedules[index].time+" "+result.schedules[index].duration);
                    
                    a = DSD(result.schedules[index].year,result.schedules[index].month,result.schedules[index].date,result.schedules[index].time,result.schedules[index].duration,
                        req.body.year,req.body.month,req.body.date,req.body.time,req.body.duration);
                    console.log("aaa : "+a);
                    if(a==false){ // 겹치는 일정이 있으면
                        console.log("################## 내 스케줄과 중복");
                        break;
                    }
                }
            }
        }}
    });
    User.findById(req.body.receiver,output,(err,result)=>{
        if(err){console.log(err)}else{if(result==null){console.log("NULL!!")}else{
            console.log("############# 상대 스케줄 중복 확인");
            //console.log(result);
            //console.log(result.schedules);
            console.log("길이: "+result.schedules.length);
            if(result.schedules.length==0){
            }else{
                for(index in result.schedules){
                    console.log("상대 일정: "+result.schedules[index].year + " "+result.schedules[index].month + " "+result.schedules[index].date +" "+ result.schedules[index].time+" "+result.schedules[index].duration);
                    b = DSD(result.schedules[index].year,result.schedules[index].month,result.schedules[index].date,result.schedules[index].time,result.schedules[index].duration,
                        req.body.year,req.body.month,req.body.date,req.body.time,req.body.duration);
                    console.log("aaa : "+a);
                    if(b==false){ // 겹치는 일정이 있으면
                        console.log("################## 상대방 스케줄과 중복");
                        break;
                    }
                }
            }
        }}
        if(a && b){
            console.log("###### 중복 없음 - REQUEST 성공  ######");
            new_request = {"sender_id":req.body._id,"sender_name":req.body.sender_name,"sender_image":req.body.sender_image,"receiver":req.body.receiver,
            "year":req.body.year,"month":req.body.month,"date":req.body.date,"time":req.body.time,"duration":req.body.duration,
            "todo":req.body.todo,"location":req.body.location,"friend":req.body.friend,"message": req.body.message,"accepts":false}
            insert_Request(new_request);
            console.log("###### RES 200");
            res.json({"ResponseCode":200});
        }else{
            console.log("###### RES 400");
            res.json({"ResponseCode":400});
        }
    });
    
       
});
app.get('/request/receive/:_id',(req,res)=>{ // _id로 나한테 온 요청 탐색
    console.log("############## 나에게 온 요청 탐색 ##############");
    var query = {receiver:req.params._id,accepts:false}; 
    var output = ['_id','username','year','month','date','time','duration','message','sender_id','image_url','sender_name','sender_image'];
    Request.find(query,output,(err,result)=>{if(err){console.log(err);}else{if(result==null){console.log("NULL!!");}else{ // 온 요청
        console.log("1차");
        for(index in result){
            //console.log("Result[INDEX]: " + index + " : "+result[index]);
            query = {_id:result[index].sender_id}; // sender_id의 image_url, username 탐색
            console.log("!!!"+result[index].sender_id);
            output = ['username','image_url','-_id'];
            User.findById(result[index].sender_id,output,(err,result2)=>{if(err){console.log(err);}else{if(result2==null){console.log("NULL!!");}else{ // user 못찾음
                console.log("RESULT 2 : "+result2); 
                console.log(result2.username);
                console.log(result2.image_url);
                console.log("RESULT \n"+result[index]);
            }}});
            //console.log("RESULT \n"+result[index]);
        }
        console.log("FINAL: "+result);
        res.json(result);
    }}});
});
app.get('/request/accept/:_id',(req,res)=>{ // 
    query = {receiver:req.params._id,accepts:true};
    var output = ['username','year','month','date','time','duration','message'];
    Request.find(query,(err,result)=>{if(err){console.log(err);}else{if(result==null){console.log("NULL!!");}else{ // 보낸 요청에 대한 수락 메시지
        console.log(result);
        res.json(result);
    }}});
});
app.get('/request/sent/:_id',(req,res)=>{ // 
    query = {sender_id:req.params._id};
    var output = ['username','year','month','date','time','duration','message'];
    Request.find(query,(err,result)=>{if(err){console.log(err);}else{if(result==null){console.log("NULL!!");}else{ // 내가 보낸 요청들
        console.log(result);
        res.json(result);
    }}});
});
app.get('/request/:_id/:accept',(req,res)=>{ // accept true -> 해당 request (by _id) accept true로,  accept false -> 해당 request 삭제
    console.log("############## 요청 수락/거부 #########");
   
    if(req.params.accept){ // sender_id,, receiver에 일정 추가
        Request.findById(req.params._id,(err,result)=>{if(err){console.log(err);}else{if(result==null){console.log("NULL!!");}else{
            console.log(result);
            result.accepts = true;
            result.save();
            console.log("요청 수락 " + result);
            res.json({"ResponseCode":200});
        
            User.findByIdAndUpdate(result.sender_id,{$push:{schedules:result}},(err,result2)=>{if(err){console.log(err);}else{if(result==null){console.log("NULL!!");}else{
                console.log(result2);
            }}});
            User.findByIdAndUpdate(result.receiver,{$push:{schedules:result}},(err,result2)=>{if(err){console.log(err);}else{if(result==null){console.log("NULL!!");}else{
                console.log(result2);
            }}});
        }}});
    }else{
        Request.findByIdAndRemove(query,(err,result)=>{if(err){console.log(err);}else{if(result==null){console.log("NULL!!");}else{
            console.log("요청 거부 "+result);
            res.json({"ResponseCode":400});
        }}});
    }
});


app.post('/upload', upload.single('image'), (req, res) => { // 이미지 저장 - filename, path(imageUrl)로 DB에 저장
    console.log("####### UPLOAD ########");
    console.log(req.file);
    console.log(req.file.path);
    res.json(req.file.path);
});
app.get('/images/:id',function(req, res){ // id - 이미지 이름(확장자 뺀 상태)
	console.log("################# GET IMAGE ###################");
	var register_number = req.params.id;
	console.log(req.params + register_number);
	var filename = register_number; //+".jpg"
	var filePath = __dirname + "/images/" + filename;
	fs.readFile(filePath,
		function (err, data)
        {	
        	if(err){
                console.log("ERROR!!");
                
				console.log(err);
				filename = "1.jpeg";
				filePath = __dirname  + "/images/" + filename;
		    	fs.readFile(filePath,
				function (err, data)
				{
					console.log(filePath);
					console.log(data);
					res.end(data);
				});
            }else{
			console.log(filePath);
			console.log(data);
			res.end(data);
			}				
        }
	);		
});


function DSD(year,month,date,time,duration,_year,_month,_date,_time,_duration){ // Detect Schedule Duplication, 앞이 기존 일정
    time_hold = time%100 + parseInt(time/100) * 60;
    _time_hold = _time%100 + parseInt(_time/100) * 60;
    //duration_hold = duration * 1;
    //_duration_hold = _duration * 1;
    if((year == _year) && (month == _month) && (date == _date)){
        if(time_hold > _time_hold){
            if(time_hold < _time_hold + _duration){
                return false;
            }else{
                return true
            }
        }else if(_time>time_hold){
            if(time_hold + duration > _time_hold){
                return false;
            }else{
                return true
            }
        }
    }else{
        return true;
    }
};
async function insert_User(_json){const user = await User.create(_json);return user;};
async function insert_Request(_json){const user = await Request.create(_json)};
async function insert_Board(_json){const user = await Board.create(_json)};


app.get('/test/:id',(req,res)=>{  // ################   테스트용 get  #################
    console.log("############# TEST #############")
    find = {_id:req.params.id};
    update = {$push:{friends:"62cc7b8844e38747c54c5213"}};
    User.findOneAndUpdate(find,update,(err,result)=>{
    if(err){
        console.log(err);
    }else{
        if(result==null){
            console.log("NI::");
        }else{
            console.log(result);
            res.json(result);
        }
    }
   })
});