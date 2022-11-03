import { css } from '@emotion/react'

const SellectInputStyle = css`
`
interface IProps {
    setType: (major: string) => void;
    typeList: Array<string>;
}
  
function SellectInput({setType, typeList}: IProps){
    const onInputHandler = (event: any) => {
        setType(event.target.value);
    };

    return (
        <select css = {SellectInputStyle} onChange={onInputHandler}>
            {typeList.map((component, index) => (
                <option value = {component} key = {index}>
                    {component}
                </option>
            ))}
        </select>
    )
    
}

export default SellectInput;