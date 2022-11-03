import { css } from '@emotion/react'
import React from 'react';
import Color from '../../layout/globalStyle/globalColor';

const ButtonStyle = css`
    text-align: center;
    background-color: ${Color.blueClassic};
    color: white;
    border-color: ${Color.blueClassic};
    border-style: solid;
    border-radius: 1em;
    height: 10vh;
    display: inline-block;
    font-family: sans-serif;
    font-weight: 100;
    max-height: 50px;
    min-width: 80px;
    font-size: 100%;
    &:hover {
        opacity: 70%;
    }
`
interface IProps {
    text: string | undefined;
    onClick: () => void;
}
  
function BasicButton({text, onClick}: IProps){
    return (
        <button css = {ButtonStyle} onClick = {onClick} id = "basic"> {text} </button>
    )
}

export default BasicButton;