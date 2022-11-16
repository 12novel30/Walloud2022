import { useRecoilValue } from "recoil"
import { personListState, PersonProps, EventProps, currentTravelState } from "../../recoils/travel"
import InputContainer from '../../layout/container/inputContainer';
import SignInput from '../../component/input/signInput';
import BasicButton from '../../component/button/basicButton';
import CreateEventAPI from "../../api/createEventAPI";
import { css } from '@emotion/react'
import { useState, useEffect } from "react";

const EventModifyStyle = css`
    display: flex;
    flex-direction: column;
    align-items: center;
    color: #fff;
`

export const EventCreate = () => {
    const currentDate = (new Date()).toISOString().substring(0, 10);
    const partiList = useRecoilValue(personListState);
    const travelId = useRecoilValue(currentTravelState);
    const [place,setPlace] = useState("");
    const [price,setPrice] = useState("10000");
    const [date,setDate] = useState(currentDate);
    const [payer, setPayer] = useState(partiList[0].personId);
    const [participants, setParticipants] = useState([...partiList])

    function EventPartiList(): JSX.Element[] {

        const checkHandler = (checked: boolean, elem:PersonProps) => {
            if (checked) {
              // participants.push(elem);
              setParticipants(() => [...participants, elem]);
            } else {
              // participants = participants.filter((e) => e.personId !== elem.personId);
              setParticipants(() =>
                [...participants].filter((e) => e.personId !== elem.personId)
              );
            }
            console.log("check partilist : ", participants );
        };

        const chk_parti = partiList.map((parti, id) => {
                return (
                    <div
                    key={id}
                    >
                        <input className="checkbox"
                        defaultChecked
                        type="checkbox"
                        id={parti.personId.toString()}
                        onChange={(e) => checkHandler(e.target.checked, parti)}
                        />
                        <label className="checkbox-text"
                        htmlFor={parti.personId.toString()}
                        style = {{ width: "30%" }}>
                            {parti.name + ""}
                        </label>
                        <input
                        id={`${parti.personId.toString}-spent`}
                        style={{ display: "inline-block", width: "40%" }}
                        value={parseInt(price) / participants.length}
                        />
                    </div>
                )
            })
        return chk_parti;
    }

    const onPartiHandler = (event:any) => {
        setPayer(event.target.value);
    }

    const onClick = () => {
        let newEvent : EventProps = {
            eventId : -1,
            name: place,
            price: parseInt(price),
            payerName: "",
            date: new Date(date),
            isDetail: false,
            partiList : []
        };
        let PartiRequired : {
            personId: number,
            role: boolean,
            spent: number
        }[] = participants.map((e) => {
            return {
                personId : e.personId,
                role: e.personId === payer,
                spent: parseInt(price) / participants.length  
            }}
        )
        CreateEventAPI(travelId, newEvent, payer, PartiRequired)
    }

    return (
        <div css = {EventModifyStyle}>
            <div>
            <h1>Create Events</h1>
            </div>
            <InputContainer>
                <SignInput name={place} text = "ex) 서울" setType= {setPlace} message = "Place" required = {true} />
                <SignInput name={price} text = "10000" setType = {setPrice} message = "Price" required = {true} />
                <SignInput name={date} text = "" setType = {setDate} message="Date" required = {true} />
                <div>
                {"Payer"}
                <br/>
                    <select onChange={onPartiHandler}>
                        {partiList.map((person, index) => (
                            <option value = {person.personId} key = {index}>
                                {person.name}
                            </option>
                        ))}
                    </select>
                </div>
            </InputContainer>
            <br/>
            {EventPartiList()}
            <br/>
            <BasicButton text="저장" onClick = {onClick}/>
        </div>
    )
}