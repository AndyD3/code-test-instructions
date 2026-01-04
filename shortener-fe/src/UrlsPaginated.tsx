import { useEffect, useState } from "react"
import { UrlsTable } from "./UrlsTable";
import { usePaginatedUrls } from './hooks/usePaginatedShortenedUrls'

type Props = {
    deleteUrl: (arg0: string) => void
}  

export const UrlsPaginated = ({deleteUrl}: Props) => {

    const [page, setPage]=useState(0);
    const [size, setSize]=useState(5);
    //ASC DESC
    const [direction, setDirection]=useState("DESC");
    //ALIAS FULLURL
    const [sort, setSort]=useState("FULLURL");  
  
    const { data: urlsPage, isLoading, isError, refetch} = usePaginatedUrls(page, size, direction, sort);

    useEffect(() => {
      refetch()
    }, [page, size, direction, sort, refetch]);


    const getPageOptions = (totalPages: number) => {
      
      const content = [];

      for (let i = 0; i < totalPages; i++) {
        content.push(<option value={i}>{i+1}</option> );
      }
      return content;
    }

    return (
    <>

      Page:
      <select
        value={page} 
        onChange={e => setPage(parseInt(e.target.value))} 
      >        
        {getPageOptions(urlsPage?.totalPages)}
      </select>

      Page Size:
      <select
        value={size} 
        onChange={e => setSize(parseInt(e.target.value))} 
      >
        <option value="2">2</option> 
        <option value="5">5</option>
        <option value="10">10</option>
        <option value="15">15</option>
        <option value="25">25</option>
        <option value="50">50</option>
      </select>

      Sort Field:
      <select
        value={sort} 
        onChange={e => setSort(e.target.value)} 
      >
        <option value="ALIAS">Alias</option>
        <option value="FULLURL">Full URL</option>
      </select>

      Sort Direction:
      <select
        value={direction} 
        onChange={e => setDirection(e.target.value)} 
      >
        <option value="DESC">Descending</option>
        <option value="ASC">Ascending</option>
      </select>
    
      {isError && <div style={{color: "red"}}>An error has occurred reading URLs...</div>}
      {isLoading && <div>Loading URLs...</div>}    
      
      <UrlsTable shortenedUrls={urlsPage?.content} deleteUrl={deleteUrl}/>
    </>

    )
}