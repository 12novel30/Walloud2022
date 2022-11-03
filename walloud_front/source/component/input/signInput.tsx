import { css } from '@emotion/react'
import Color from '../../layout/globalStyle/globalColor';
import { FontSize } from '../../layout/globalStyle/globalSize';

const InputStyle = css`
    &>div {
        padding: 0 5px;
        font-weight: 300;
        color: white;
        &>span {
            color: red;
        }
    }

    &>input {
    display: block;
    width: 300px;
    height: 4.5vh;
    margin: 2% auto 3% auto;
    border-radius: 5px;
    border: 1px solid ${Color.gray05};
    font-size: ${FontSize.fs08};
    font-family: sans-serif;
    font-weight: 200;
    padding-left: 10px;
    }
`
interface IProps {
    name: string | undefined;
    text: string;
    setType: (major: string) => void;
    message: string;
    required: boolean;
}
  
function SignInput({name, text, setType, message, required}: IProps){
    const onInputHandler = (event: any) => {
        setType(event.target.value);
    };

    return (
        <div css = {InputStyle}>
            <div>{message}<span>{required ? " *" : ""}</span></div> 
            <input type = {message} value = {name} onChange = {onInputHandler}
            placeholder = {text} />
        </div>
    )
    
}

export default SignInput;