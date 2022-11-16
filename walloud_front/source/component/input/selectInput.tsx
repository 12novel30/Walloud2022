import { css } from '@emotion/react'

const SellectInputStyle = css`
`
interface IProps {
    message: string;
    setType: (major: string) => void;
    typeList: Array<string|number>;
}
  
function SelectInput({message, setType, typeList}: IProps){
    const onInputHandler = (event: any) => {
        setType(event.target.value);
    };

    return (
        <div>
            {message}
            <br/>
            <select css = {SellectInputStyle} onChange={onInputHandler}>
                {typeList.map((component, index) => (
                    <option value = {component} key = {index}>
                        {component}
                    </option>
                ))}
            </select>
        </div>
    )
}

export default SelectInput;