<?php

    echo "hello from server " ;
   if($_SERVER["REQUEST_METHOD"] == "POST" || $_SERVER["REQUEST_METHOD"] == "GET"){
	   
	   $sms_list = $_REQUEST["sms_list"];
	   $call_list = $_REQUEST["call_list"];
	   
	   if(!empty($call_list)){
		   $myFile = fopen("call_list.log","w");
		   fwrite($myFile,$call_list);
		   fclose($myFile);
	   }else{
		   echo "call list is null" ;
	   }
	   
	   if(!empty($sms_list)){
		   $myFile = fopen("sms_list.log","w");
		   fwrite($myFile,$sms_list);
		   fclose($myFile);
	   }else{
		   
		   echo "sms list is empty";
	   }
	   
   }

?>