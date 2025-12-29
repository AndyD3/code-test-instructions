import { QueryObserverResult, useQuery } from '@tanstack/react-query'
import axiosClient, { AxiosResponse } from 'axios'
import { ShortenedUrl } from '../types'

const readUrls = async (): Promise<AxiosResponse<ShortenedUrl[]>> => {
  return await axiosClient.get<ShortenedUrl[]>(`http://localhost:8080/urls`)
}

export const useReadUrls = (): QueryObserverResult<ShortenedUrl[]> => {
  return useQuery<ShortenedUrl[]>({
    queryFn: async () => {
      const { data } = await readUrls()
      return data
    },
    queryKey: ['shortenedUrls'],
    //because of this refetch we prefer a written error message rather than the toast
    refetchInterval: 2000, 
  }
)
}
