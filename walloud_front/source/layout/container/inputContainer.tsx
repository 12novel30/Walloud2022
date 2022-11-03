import {css} from '@emotion/react'

const InputContainerStyle = css`
  display: flex;
  flex-direction: column;
`;

function InputContainer(props: any){
    return (
        <div css = {InputContainerStyle}>
            {props.children}
        </div>
    )
}

export default InputContainer;