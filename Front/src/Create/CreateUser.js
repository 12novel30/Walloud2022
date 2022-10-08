import axios from "axios";
import React, { useRef, useState } from "react";
import { Link, useNavigate, useParams } from "react-router-dom";

const UserInvite = ({friend, onDel}) => {

  return (
    <li>
      <div>
        <span>
          <strong>이메일 : {friend.email}</strong>
          <button onClick = {() => onDel(friend.id)}>삭제</button>
        </span>
      </div>
    </li>
  )
}

const CreateUser = () => {

  // multi input 만들기
  const { travel, travelName, user } = useParams();
  const navigate = useNavigate();
  const no = useRef(2);
  const inviteRef = useRef();
  const [inviteList,setInvite] = useState([]);
  const [form, setFrom] = useState({
    email : ""
  });
  
  const changeInput = (e) => {
    const {value, name} = e.target
    setFrom({
      ...form,
      [name] : value
    })
  }

  const onSubmit = (e) => {
    e.preventDefault();
    if(!email) {
      alert("Write any email!");
      return;
    }
    onAdd(form)

    setFrom({
      email : ''
    })
    inviteRef.current.focus()
  }
  
  const {email} = form;

  const onAdd = (form) => {
    form.id = no.current++;
    setInvite(inviteList.concat(form));
  }
  
  const onDel = (id) => {
    setInvite(inviteList.filter(e => e.id !== id))
  }

  const sendInvite = async() => {
    let need_to_send = [...inviteList];
    console.log("Before axios : ",need_to_send);
    await inviteList.map((invite_elem) => {
      console.log(invite_elem.id);
      axios.post(`/api/${user}/${travel}/createUser`,{
        user_email : invite_elem.email
      }).then((res) => {
        console.log(res.data);
        need_to_send = need_to_send.filter(e => e.id !== invite_elem.id);
        console.log("invite List : ", need_to_send);
        setInvite(need_to_send);
        if(need_to_send.length === 0) {
          console.log("test")
          navigate(`/${user}/${travel}/${travelName}`,{state : {created : false}});
        }
      }).catch((error) => {
        if (error.response.data.status === 500) {
          alert(invite_elem.email+error.response.data.message);
        }
      })
    })
  }


  return (
    <div>
      <Link
        to={`/${user}/${travel}/${travelName}`}
        state = {{created : false}}
      >
      
       <h1 className="home">{travelName}</h1>
     </Link>
     <h2>Add User</h2>
      <h3>총인원 : {inviteList.length}</h3>
      <form className="formadd" onSubmit={onSubmit}>
        <span><label htmlFor="email">email</label> : <input type="email" id="email" onChange={changeInput} value={email} name="email" ref={inviteRef}/> <button type="submit">추가</button></span>
      </form>
      <ul>
        {inviteList && inviteList.map(friend => <UserInvite key={friend.id} friend={friend} onDel = {onDel}/>)}
      </ul>
      <button onClick= {sendInvite}>Submit</button>

    </div>
  )

}


export default CreateUser;
