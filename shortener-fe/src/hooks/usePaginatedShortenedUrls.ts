
import { QueryObserverResult, useQuery } from '@tanstack/react-query'
import axiosClient, { AxiosResponse } from 'axios'
import { ShortenedUrl } from '../types'
  
const readUrls = async (page = 0, size = 5, sortDirection = "ASC", sortField="ALIAS"): Promise<AxiosResponse<ShortenedUrl[]>> => {
    return await axiosClient.get<ShortenedUrl[]>(`http://localhost:8080/paginatedUrls?page=${page}&sizePerPage=${size}&sortDirection=${sortDirection}&sortField=${sortField}`)
  }
  
  export const usePaginatedUrls = (page: number, size:number, sortDirection:string, sortField:string): QueryObserverResult<ShortenedUrl[]> => {
    return useQuery<ShortenedUrl[]>({
      queryFn: async () => {
        const { data } = await readUrls(page, size,sortDirection, sortField)
        return data
      },
      queryKey: ['shortenedUrls'],
    }
  )
  }