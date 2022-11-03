import React, {useState, useEffect} from "react"

// 'top', 'down', 'up'
function ScrollNavi(){
    const [scrollMode, setScrollMode] = useState('top');
    let beforeScrollY = 0;
    useEffect(() => {
        window.addEventListener('scroll', handleScroll);
        return () => {
            window.removeEventListener('scroll', handleScroll);
        }
    }, []);
    const handleScroll = () => {
        let directionScroll = (window.scrollY - beforeScrollY > 0);
        window.scrollY === 0 ? setScrollMode('top') : 
        (directionScroll ? setScrollMode('down') : setScrollMode('up'));
        beforeScrollY = window.scrollY;
        }

    return scrollMode;
}

export default ScrollNavi;