import {css} from "@emotion/react"

function Logo(height: any, color: any){
    const LogoStyle = css`
        height: ${height};
        filter: ${color};
    `

    return (
        <a href='/'>
            <img src = '/source/assets/logo-main.svg'
            css = {LogoStyle}/>
        </a>
    );
}

export default Logo;